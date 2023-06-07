/* Q9 [15 points] (q9_9th_decile_ratings):
For all people born in 1955, get their name and average rating on all movies they have been part of through their careers. Output the 9th decile of individuals as measured by their average career movie rating.
Details: Calculate average ratings for each individual born in 1955 across only the movies they have been part of. Compute the quantiles for each individual's average rating using NTILE(10).
Make sure your output is formatted as follows (round average rating to the nearest hundredth, results should be ordered by a compound value of their ratings descending and secondly their name in alphabetical order): Stanley Nelson|7.13
Note: You should take quantiles after processing the average career movie rating of individuals. In other words, find the individuals who have an average career movie rating in the 9th decile of all individuals. */

with result as (
    select p.name as name, round(avg(r.rating), 2) as rating
    from people as p
    join crew as c on p.person_id = c.person_id
    join titles as t on t.title_id = c.title_id
    join ratings as r on r.title_id = t.title_id
    where p.born = "1955" and t.type = "movie"
    group by name
)

select r.name, r.rating from (
    select ntile(10) over(order by rating asc) as ntile_rating, * 
    from result
) as r
where r.ntile_rating = 9
order by rating desc, name asc;

/* Michiel Romeyn|7.3
Paula Anglin|7.3
Mani Ratnam|7.27
Charo Santos-Concio|7.25
Uri Gavriel|7.17
Marilou Diaz-Abaya|7.15
Stanley Nelson|7.13
Jeremiah S. Chechik|7.1
Marie Gruber|7.1
Ric Burns|7.1
Dana Carvey|7.05
Garry Bushell|7.05
Ildikó Enyedi|7.0 */