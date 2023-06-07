/* Q7 [15 points] (q7_year_of_thieves):
List the number of works that premiered in the same year that "Army of Thieves" premiered.
Details: Print only the total number of works. The answer should include "Army of Thieves" itself. For this question, determine distinct works by their title_id, not their names. */

select count(1)
from titles
where premiered in (
    select premiered
    from titles
    where primary_title = "Army of Thieves"
);

/* 63843 */