# Raamatukogu rakendus

Lihtne raamatukogu rakendus, kus saab REST api abil hallata raamatuid, lugejaid ja laenutusi.

Rakenduse üles seadmiseks on vaja rakenduse lähtekood kloonida ning ühendada PostegreSQL andmebaasiga (rakendus loodud v14.5 kasutades). Seejärel saab rakenduse käivitada. 

Reposse lisatud ka *raamatukogu.postman_collection.json* fail, mida Postman'i importides võimalik saada nö päringute stardipakett.

Võimalike REST api päringute nimekiri: 

**/api/books - POST** - salvestab sisendiks antud raamatu, tagastab salvestatud raamatu objekti

**/api/books - GET**  - tagastab kõikide salvestatud raamatute hulga

**/api/books/{id} - GET** - tagastab etteantud id-ga raamatu, kui see eksisteerib

**/api/books/{id} - PUT** - uuendab raamatu andmeid vastavalt sisendile

**/api/books/{id} - DELETE** - Kustutab etteantud id-ga raamatu objekti, kui see eksisteerib

**/api/books/public - GET** - Tagastab avaliku vaate rakenduse raamatutest, näitab mitmeks nädalaks on raamatut võimalik laenutada, ainuke meetod mida on võimalik vaadata ilma kasutajaõigusteta

**/api/books/search/{keyword} - GET** - tagastab märksõnaga sobivate raamatute hulga, otsitakse raamatu nime, autori ja isbn koodi põhjal

**/api/readers - POST** - salvestab sisendiks antud lugeja, tagastab salvestatud lugeja objekti

**/api/readers - GET** - tagastab kõikide salvestatud lugejate hulga

**/api/readers/{id} - GET -** tagastab etteantud id-ga lugeja objekti, kui ta eksisteerib

**/api/readers/{id} - PUT** - uuendab lugeja andmeid vastavalt sisendile

**/api/readers/{id} - DELETE** - Kustutab etteantud id-ga lugeja, kui ta eksisteerib

**/api/readers/search/{keyword} - GET** - tagastab märksõnaga sobivate lugejate hulga, otsitakse raamatu eesnime ja perenime põhjal

**/api/rentals - POST** - salvestab sisendiks antud raamatu id ja lugeja id põhjal laenutuse, arvutab laenutuse tähtaja kui seda ette pole antud, salvestab laenutaja täisnime, teeb laenutuse aktiivseks ning arvab laenatava raamatu saadavalolevatest maha, tagastab salvestatud laenutuse objekti

**/api/rentals - GET** - tagastab kõikide salvestatud laenutuste hulga

**/api/rentals/{id} - GET** - tagastab etteantud id-ga laenutuse objekti, kui see eksisteerib

**/api/rentals/{id} - PUT** - uuendab laenutuse tähtaega vastavalt sisendile, tagastab laenutuse objekti muudetud kujul

**/api/rentals/close/{id} - PUT** - Lõpetab laenutuse, teeb laenutuse mitteaktiivseks ning lisab raamatu tagasi saadavalolevate sekka, tagastab mitteaktiivse laenutuse objekti 

**/api/rentals/overdue - GET** - Tagastab raporti üle tähtaja läinud aktiivsetest laenutustest

**/api/rentals/active - GET** - Tagastab aktiivsete laenutuste hulga
