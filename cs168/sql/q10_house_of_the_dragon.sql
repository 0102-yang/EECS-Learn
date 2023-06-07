/* Q10 [15 points] (q10_house_of_the_dragon):
Concatenate all the unique titles for the TV Series "House of the Dragon" as a string of comma-separated values in alphabetical order of the titles.
Details: Find all the unique dubbed titles for the new TV show "House of the Dragon" and order them alphabetically. Print a single string containing all these titles separated by commas.
Hint: You might find Recursive CTEs useful.
Note: Two titles are different even if they differ only in capitalization. Elements in the comma-separated value result should be separated with both a comma and a space, e.g. "foo, bar". */

with akas_titles as (
    select row_number() over(order by title asc) as row_number, *
    from (
        select distinct title from akas where title_id in (
            select title_id from titles
            where primary_title = "House of the Dragon" and type="tvSeries"
        )
    )
),
result as (
    select row_number, title
    from akas_titles
    where row_number = 1
    union all
    select a.row_number, r.title || ', ' || a.title
    from akas_titles as a
    join result as r
    on a.row_number = r.row_number + 1
)

select title
from result
where row_number = (select count(*) from akas_titles);

/* A Casa do Dragão, A Guerra dos Tronos: A Casa do Dragão, Dom smoka, Game of Thrones: A Casa do Dragão, Gia Tộc Rồng, House of the Dragon, La Casa del Dragón, La casa del dragón, Rod draka, Ród smoka, Sárkányok háza, Zmajeva kuća, Дом Дракона, Домът на дракона, Дім Дракона, Кућа змаја, בית הדרקון, آل التنين, ハウス・オブ・ザ・ドラゴン, 龍族前傳, 하우스 오브 드래곤 */