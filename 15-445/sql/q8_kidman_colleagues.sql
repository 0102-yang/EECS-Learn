/* Q8 [15 points] (q8_kidman_colleagues):
List the all the different actors and actresses who have starred in a work with Nicole Kidman (born in 1967).
Details: Print only the names of the actors and actresses in alphabetical order. The answer should include Nicole Kidman herself. Each name should only appear once in the output.
Note: As mentioned in the schema, when considering the role of an individual on the crew, refer to the field category. The roles "actor" and "actress" are different and should be accounted for as such. */

with kidman_movies as (
    select title_id
    from crew
    where person_id in (
        select person_id
        from people
        where name = "Nicole Kidman" and born = "1967"
    )
)

select name
from people
where person_id in (
    select person_id
    from crew
    where title_id in kidman_movies and (category = "actor" or category = "actress")
)
order by name asc;

/* Betty Gilpin
Casey Affleck
Colin Farrell
Crista Flanagan
Danny Huston
Dennis Miller
Donald Sutherland
Ed Mantell
Fionnula Flanagan
Flora Cross
Fredrik Skavlan
Gus Mercurio
Halle Berry
Harris Yulin
J.K. Simmons
Jackson Bond
James Corden
Jason Bateman
Javier Bardem
Jesper Christensen
John Lithgow
Julianne Moore
Kai Lewins
Kyle Mooney
Lisa Flanagan
Liz Burch
Mahershala Ali
Maria Tran
Mark Strong
Nicholas Eadie
Nicole Kidman
Paul Bettany
Pauline Chan
Robert Pattinson
Russell Crowe
Sam Neill
Shailene Woodley
Sherie Graham
Simon Baker
Stellan Skarsgård
Tom Cruise
Valerie Yu
Veronica Lang
Will Ferrell */