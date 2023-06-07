/* Q6 [10 points] (q6_cruiseing_altitude):
Determine the most popular works with a person who has "Cruise" in their name and is born in 1962.
Details: Get the works with the most votes that have a person in the crew with "Cruise" in their name who was born in 1962. Return both the name of the work and the number of votes and only list the top 10 results in order from most to least votes. Make sure your output is formatted as follows: Top Gun|408389 */

select t.primary_title as name, r.votes as votes
from titles as t join ratings as r
on t.title_id = r.title_id
where t.title_id in (
    select title_id from crew where person_id in (
        select person_id from people
        where (name like "%Cruise%" or name like "%cruise%")
        and born = "1962"
    )
)
order by votes desc
limit 10;

/* Oblivion|520383
Mission: Impossible|423228
Top Gun|408389
Magnolia|311030
Born on the Fourth of July|106667
Days of Thunder|88698
Lions for Lambs|50257
Without Limits|7127
Space Station 3D|1693
Nickelodeon Kids' Choice Awards 2012|212 */