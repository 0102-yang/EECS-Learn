/* Q2 [5 points] (q2_sci_fi):
Find the 10 `Sci-Fi` works with the longest runtimes.
Details: Print the title of the work, the premiere date, and the runtime. The column listing the runtime should be suffixed with the string " (mins)", for example, if the runtime_mins value is `12`, you should output 12 (mins). Note a work is Sci-Fi even if it is categorized in multiple genres, as long as Sci-Fi is one of the genres.
Your first row should look like this: Cicak-Man 2: Planet Hitam|2008|999 (mins) */

select primary_title, premiered, runtime_minutes || " (mins)"
from titles
where genres like "%Sci-Fi%"
order by runtime_minutes desc
limit 10;

/* Cicak-Man 2: Planet Hitam|2008|999 (mins)
Project Hail Mary|2021|970 (mins)
Wholy|2018|660 (mins)
Tales from the Void|2016|600 (mins)
Blade Runner: Czy androidy marza o elektrycznych owcach? (Audioplay)|2012|403 (mins)
Cold Lazarus|1996|300 (mins)
Phantom Gear|2021|300 (mins)
The Halt|2019|279 (mins)
V: The Final Battle|1984|272 (mins)
Atom Man vs. Superman|1950|252 (mins) */