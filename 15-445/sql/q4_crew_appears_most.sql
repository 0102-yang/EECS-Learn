/* Q4 [10 points] (q4_crew_appears_most):
Find the people who appear most frequently as crew members.
Details: Print the names and number of appearances of the 20 people with the most crew appearances ordered by their number of appearances in a descending fashion.
Your output should look like this: NAME|NUM_APPEARANCES */

select p.name as name, count(1) as num_appearances
from people as p join crew as c
on p.person_id = c.person_id
group by p.name
order by num_appearances desc
limit 20;

/* Nivedita Basu|12564
Joel Beckerman|11286
Vic Sotto|10626
Ed McMahon|10153
Sameera Sherief|9905
Johnny Gilbert|9677
Erwin Romulo|8774
Ernesto Alonso|8471
Go Haruna|8454
Bradley Bell|8262
Helio Bannwart|7429
Danie Joubert|7340
Ginji Itô|7059
Shôichi Kuroki|7053
Duma Ndlovu|6865
Vaidehi Ramamurthy|6806
Sharad Tripathi|6649
Adrián Suar|5939
Mrinal Jha|5869
Jada Rowland|5763 */