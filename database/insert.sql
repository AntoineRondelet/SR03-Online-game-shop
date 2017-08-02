-- to create and insert all data in the DB
-- $ psql -p 2472 -d sr03 -f ./create.sql -f ./insert.sql


BEGIN;

--- Ajout des utilisateurs Antoine Rondelet et Pierre-Louis Lacorte pour faire nos tests ---
\echo  "ajout des utilisateurs";
INSERT INTO CUSTOMER VALUES ('arondele', 'Antoine', 'Rondelet', 'antoine@rondelet.com', 'passwordtest', TRUE, '1995-10-30', '2017-04-04');
INSERT INTO CUSTOMER VALUES ('placorte', 'Pierre-Louis', 'Lacorte', 'pierre@louis.com', 'passwordtestbis', FALSE, '1995-11-11', '2017-04-05');

--ajout des modes de payement--
\echo  "ajout des modes de payment";
INSERT INTO PAYMENT_MODE VALUES ('Visa');
INSERT INTO PAYMENT_MODE VALUES ('Mastercard');
INSERT INTO PAYMENT_MODE VALUES ('Paypal');
INSERT INTO PAYMENT_MODE VALUES ('Bitcoin');

--ajout des status de payement--
\echo  "ajout des status de payment";
INSERT INTO PAYMENT_STATUS VALUES ('Waiting');
INSERT INTO PAYMENT_STATUS VALUES ('Done');

--ajout des keywords--
\echo  "ajout des keywords";
INSERT INTO GAME_KEYWORD VALUES ('War');
INSERT INTO GAME_KEYWORD VALUES ('Family');
INSERT INTO GAME_KEYWORD VALUES ('Adventure');
INSERT INTO GAME_KEYWORD VALUES ('Sport');

--ajout d'achat--
\echo  "ajout d'achat";
INSERT INTO PURCHASE(date_paid, username, payment_mode, payment_status) VALUES ('2017-04-04', 'arondele','Visa', 'Done');
INSERT INTO PURCHASE(date_paid, username, payment_mode, payment_status) VALUES ('2017-04-04', 'placorte', 'Paypal', 'Done');
INSERT INTO PURCHASE(date_paid, username, payment_mode, payment_status) VALUES ('2017-04-04', 'arondele', 'Mastercard', 'Waiting');

--ajout des consoles--
\echo  "ajout des consoles"
INSERT INTO GAME_CONSOLE VALUES ('PS3', '2015-02-02', 'Slim', 'Description of the model', 256);
INSERT INTO GAME_CONSOLE VALUES ('PS4', '2017-03-03', 'Pro', 'Description of the model', 128);
INSERT INTO GAME_CONSOLE VALUES ('Wii', '2014-01-01', 'Premium edition', 'Description of the model', 64);
INSERT INTO GAME_CONSOLE VALUES ('XBOX360', '2013-12-02', 'Starter pack', 'Description of the model', 256);

--ajout des jeux-- {titre et age limite}
\echo "ajout des jeux";
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Zelda and the legend of the princess', 'PS3', 6, 45, '2015-10-02', 'Entrez dans un monde d''aventure Oubliez tout ce que vous savez sur les jeux The Legend of Zelda. Plongez dans un monde de découverte, d''exploration et d''aventure dans The Legend of Zelda: Breath of the Wild, un nouveau jeu qui vient bouleverser la série à succès. Voyagez à travers champs, traversez des forêts et grimpez sur des sommets dans votre périple où vous explorez le royaume d''Hyrule en ruines à travers cette aventure à ciel ouvert.', 4);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Call of duty: Modern warfare 2', 'PS3', 18, 70, '2016-12-02', 'Le 6ème volet de la série des Call of Duty, suite directe de Modern Warfare, sort enfin en novembre 2009 ! Immergez vous totalement dans ce conflit international et essayez de défaire la nouvelle menace qui pèse sur la sécurité mondiale. Et aux grand maux, les grands remèdes : anéantissez tout ce qui bouge à l''aide d''un grand nombre d''armes et de compétences inédites.', 4);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Call of duty: Modern warfare 2', 'PS4', 18, 70, '2017-04-02', 'Le 6ème volet de la série des Call of Duty, suite directe de Modern Warfare, sort enfin en novembre 2009 ! Immergez vous totalement dans ce conflit international et essayez de défaire la nouvelle menace qui pèse sur la sécurité mondiale. Et aux grand maux, les grands remèdes : anéantissez tout ce qui bouge à l''aide d''un grand nombre d''armes et de compétences inédites.', 5);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Fifa 2017', 'PS4', 3, 60, '2017-01-06', 'FIFA 17 dévoile une nouvelle expérience narrative inédite bénéficiant d''un scénario réaliste, de cinématiques, ainsi qu''une innovation complète en matière de gameplay Plongés dans le tout nouveau mode de jeu L''Aventure, les joueurs vivront, sur et en dehors du terrain, l''histoire d''Alex Hunter, recrue du football cherchant à faire sa place en Premier League. ', 5);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Battlefield 3', 'PS3', 18, 35, '2015-10-02', 'Battlefield 3 prend une bonne longueur d''avance sur ses concurrents grâce à la puissance du FrostbiteT 2, le moteur nouvelle génération du studio DICE. Cette technologie innovante est à la base de Battlefield 3, et permet d''offrir une qualité graphique sans précédent, des environnements gigantesques, un niveau de destruction ahurissant, une ambiance sonore dynamique et des animations d''un réalisme époustouflant. ', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Final Fantasy', 'Wii', 10, 20, '2013-10-02', 'Plongez dans l''univers de FINAL FANTASY XV pour vivre une véritable épopée, ponctuée de combats intenses.  Dans la peau de Noctis à vous de renverse ''armée impériale, libérer votre patrie de l''envahisseur, et reconquérir le trône...', 2);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Mario Kart', 'Wii', 3, 25, '2015-08-13', 'Appuyez sur le champignon et affûtez vos carapaces, Mario Kart 8 Deluxe va tout retourner sur Nintendo Switch ! Foncez à fond les ballons la tête à l''envers avec les pneus anti-gravité ! Irez-vous plus vite en passant par le plafond ? Ou allez-vous tracer au sol entre les bananes et les batailles de carapace ? Tous les coups les plus fourbes sont permis pour se hisser à la première place ! ', 4);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Mario Tennis', 'Wii', 3, 25, '2016-11-22', 'Vivez un nouveau genre de tennis ! Renvoyez vos balles en libérant la nouvelle frappe sautée permettant d''atteindre des balles très en hauteur, ou alors utilisez un méga champignon pour décupler la taille de votre joueur. Des parties endiablées vous attendent !', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Resident Evil 4', 'Wii', 18, 35, '2013-09-16', 'Le meilleur jeu de 2005 arrive, avec encore plus d''horreur et de nouveaux éléments exclusifs. Serez-vous à la hauteur de cette nouvelle version du jeu qui a révolutionné la série acclamée des Resident Evil ? Cela fait six ans que le redoutable virus T s''est répandu. Le gouvernement américain a fait disparaître la maléfique multinationale Umbrella, la société responsable de ces tragiques évènements. ', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Virtual Tennis 5', 'PS3', 3, 15, '2014-11-02', 'Considérée comme la référence absolue des simulations nautiques, Virtual Skipper revient dans une nouvelle édition qui ravira tous les fans de voile avec en point d''orgue une magnifique campagne entièrement dédiée à l''America''s Cup, la plus prestigieuse des compétitions de voile. Prenez part à des régates pleines d''intensité à bord de bateaux d''exception sur une mer saisissante de réalisme en solo comme en multijoueurs.', 4);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Need For Speed Underground', 'PS4', 6, 50, '2012-08-22', 'Très beau! A mon avis le seul point fort de ce nouveau NFS.Reprenant certains ingrédients de certains chapitres de la série , ce dernier n''atteint malheureusement pas le plaisir et surtout l''ambiance de "Hot pursuit"...Dommage.', 5);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Sonic', 'Wii', 3, 25, '2013-08-14', 'IL VA Y AVOIR DU SPORT AVEC MARIO & SONIC ! La tournée des Jeux Olympiques continue pour Mario et Sonic qui se retrouvent pour un face-à-face survolté sous le soleil de Rio, dans Mario & Sonic aux Jeux Olympiques de Rio 2016 sur Nintendo 3DS ! Les personnages les plus emblématiques du Royaume Champignon ainsi que Sonic et tous ses amis se sont réunis pour s''affronter dans des compétitions olympiques impitoyables !', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('GTA V', 'PS4', 18, 60, '2016-01-23', 'Lorsqu''un jeune arnaqueur, un braqueur de banque à la retraite et un terrifiant psychopathe se retrouvent piégés par de grands criminels, le gouvernement américain et l''industrie du divertissement, ils décident de se lancer dans une série de braquages pour survivre dans une ville sans pitié où ils ne peuvent se fier à personne, même entre eux.', 4);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Crash vs Titans', 'PS3', 6, 25, '2015-01-22', 'Vraiment un bon jeu. j''avais un peu peur de la reprise car je suis fan de crash sur Playstation! Et bien rien a envier il est vraiment bien, les graphismes sont plutot pas mal et il y a quelques nouveautés par rapport a l''original. ', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('True Crime', 'PS4', 18, 25, '2016-11-22', 'True Crime poursuit ses investigations. Neuf stars du crime vous plongent dans neuf histoires d''amour qui finissent mal. Au cour de ce deuxième volume, les crimes sexuels et les passions fatales qui remplissent les cours d''assises et les cimetières : parce que l''amour, ce havre de paix, emprunte souvent les chemins de la guerre.', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('TAKKEN 7', 'PS4', 16, 54, '2015-01-22', 'Tous les combats ont une histoire. Quelle sera la vôtre ?Découvrez la conclusion épique de la guerre du clan Mishima et découvrez les raisons qui ont motivé chacun dans leurs combats incessants. Alimenté par Unreal Engine 4, TEKKEN 7 offre des batailles cinématiques basées sur des histoires captivantes et des duels extraordinaires à partager avec vos amis et vos rivaux grâce aux mécanismes de combats innovants.', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Mass Effect Andromeda PS4', 'PS4', 16, 50, '2016-11-22', 'Mass Effect : Andromeda embarque les joueurs vers la galaxie d''Andromède , bien au-delà de la Voie Lactée . Là-bas, les joueurs mèneront notre combat pour un nouveau foyer en terrain hostile où NOUS sommes les aliens.', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('NIOH', 'PS4', 18, 25, '2016-11-22', 'Prêt à mourir ? Alors, venez découvrir le tout nouveau jeu d''action brutal estampillé Team NINJA et Koei Tecmo Games. Incarnez un voyageur solitaire qui débarque sur les côtes du Japon des samurais et partez à la découverte d''un pays infesté de guerriers cruels et de yokai surnaturels qu''il vous faudra combattre pour trouver ce que vous êtes venu chercher.', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('The Elder Scrolls V Skyrim', 'PS4', 18, 23, '2016-11-22','Lauréat de plus de 200 récompenses du Jeu de l''année ! Skyrim Special Edition apporte un souffle nouveau à cette aventure épique, avec force détail. La Special Edition comprend le célèbre jeu et les contenus additionnels, avec graphismes et effets remasterisés, rayons divins volumétriques, profondeur de champ dynamique, reflets et plus encore.', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('The last of us', 'PS4', 18, 30, '2016-11-22', 'The Last of Us Revient sur PS4 ! Le monde que nous connaissons n''existe plus. L''humanité a été infectée par un virus transformant les hommes en bêtes sanguinaires. Vous incarnez Joel durant son périple à travers des Etats-Unis dévastés.', 5);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Uncharted 4 A Thief''s End', 'PS4', 16, 25, '2016-11-22','Chaque trésor a un prixAlors qu''il semblait enfin rangé de la chasse au trésor et promis à une vie plus tranquille depuis trois ans, Nathan Drake voit l''aventure frapper à sa porte à nouveau lorsque son frère, Sam, refait surface. Quinze années après avoir disparu, ce dernier vient réclamer son aide et lui proposer une aventure qu''il ne peut refuser : la quête du trésor du Capitaine Henry Avery.', 3);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Rise Of The Tomb Raider', 'PS4', 18, 40, '2016-10-06', 'Rise of the Tomb Raider : 20ème anniversaire comprend le jeu encensé par la critique Rise of the Tomb Raider, nominé pour plus de 75 prix, marquant l''évolution de Lara Croft de survivante à aventurière aguerrie, lorsqu''elle s''embarque dans sa première expédition de "Tomb Raider", qui l''emmènera dans les régions les plus hostiles de la Sibérie.', 5);
INSERT INTO GAME(title, console, age_limit, price, release_date, description, rate) VALUES ('Hitman', 'PS4', 18, 35, '2016-10-06', 'L''intégrale de la première saison, la version disque ultime du jeu. Livrée dans une élégante édition avec un boîtier SteelBook, elle contient toutes les destinations et tous les épisodes de la première saison de Hitman, soit : le prologue, la France, l''Italie, le Maroc, la Thaïlande, les États-Unis et le Japon. Dans ce véritable monde d''assassinats, vous incarnez l''agent 47, le tueur à gages ultime dont la mission est d''éliminer des cibles de premier plan aux quatre coins du monde. Plongez-vous dans une aventure pleine d''action et déjouez un vaste complot mondial.', 5);




--ajout des jeux a l'unité--
\echo  "ajout des jeux a l'unite";
--zelda--PS3--10 entities--2 bought
INSERT INTO GAME_ENTITY VALUES (258814, 1, 2);
INSERT INTO GAME_ENTITY VALUES (229488, 1, 2);
INSERT INTO GAME_ENTITY VALUES (639515, 1);
INSERT INTO GAME_ENTITY VALUES (144885, 1);
INSERT INTO GAME_ENTITY VALUES (593847, 1);
INSERT INTO GAME_ENTITY VALUES (913907, 1);
INSERT INTO GAME_ENTITY VALUES (580542, 1);
INSERT INTO GAME_ENTITY VALUES (646643, 1);
INSERT INTO GAME_ENTITY VALUES (438629, 1);
--Call of duty: Modern warfare 2--PS3--1 entities--
INSERT INTO GAME_ENTITY VALUES (623751, 2);
--Call of duty: Modern warfare 2--PS4--5 entities-- 1 bought
INSERT INTO GAME_ENTITY VALUES (210674, 3);
INSERT INTO GAME_ENTITY VALUES (878918, 3);
INSERT INTO GAME_ENTITY VALUES (947313, 3);
INSERT INTO GAME_ENTITY VALUES (1051, 3);
INSERT INTO GAME_ENTITY VALUES (421154, 3, 1);
--Fifa 2017--PS4--2 entities--
INSERT INTO GAME_ENTITY VALUES (947314, 4);
INSERT INTO GAME_ENTITY VALUES (421153, 4);
--Battlefield 3--PS4--5 entities-- 1 bought
INSERT INTO GAME_ENTITY VALUES (175251, 5);
INSERT INTO GAME_ENTITY VALUES (620555,	 5);
INSERT INTO GAME_ENTITY VALUES (748658, 5);
INSERT INTO GAME_ENTITY VALUES (818217, 5);
INSERT INTO GAME_ENTITY VALUES (180284, 5);
INSERT INTO GAME_ENTITY VALUES (882898, 5);
INSERT INTO GAME_ENTITY VALUES (392933, 5, 1);
INSERT INTO GAME_ENTITY VALUES (578970, 5, 1);
--Final Fantasy--Wii--4 entities-- 1 bought
INSERT INTO GAME_ENTITY VALUES (125119, 6, 2);
INSERT INTO GAME_ENTITY VALUES (863101, 6);
INSERT INTO GAME_ENTITY VALUES (903050, 6);
INSERT INTO GAME_ENTITY VALUES (246, 6);
--Mario Kart--Wii--2 entities-- 0 bought
INSERT INTO GAME_ENTITY VALUES (524185, 7);
INSERT INTO GAME_ENTITY VALUES (345673, 7);
--Mario Tennis--Wii--12 entities-- 1 bought
INSERT INTO GAME_ENTITY VALUES (227451, 8, 3);
INSERT INTO GAME_ENTITY VALUES (32046, 8);
INSERT INTO GAME_ENTITY VALUES (621742, 8);
INSERT INTO GAME_ENTITY VALUES (354907, 8);
INSERT INTO GAME_ENTITY VALUES (9960, 8);
INSERT INTO GAME_ENTITY VALUES (23908, 8);
INSERT INTO GAME_ENTITY VALUES (193043, 8);
INSERT INTO GAME_ENTITY VALUES (355796, 8);
INSERT INTO GAME_ENTITY VALUES (506286, 8);
INSERT INTO GAME_ENTITY VALUES (333984, 8);
INSERT INTO GAME_ENTITY VALUES (655831, 8);
INSERT INTO GAME_ENTITY VALUES (130146, 8);
--Resident Evil 4--Wii--11 entities-- 0 bought
INSERT INTO GAME_ENTITY VALUES (40313, 9);
INSERT INTO GAME_ENTITY VALUES (513973, 9);
INSERT INTO GAME_ENTITY VALUES (302568, 9);
INSERT INTO GAME_ENTITY VALUES (881053, 9);
INSERT INTO GAME_ENTITY VALUES (510012, 9);
INSERT INTO GAME_ENTITY VALUES (508674, 9);
INSERT INTO GAME_ENTITY VALUES (945010, 9);
INSERT INTO GAME_ENTITY VALUES (893617, 9);
INSERT INTO GAME_ENTITY VALUES (249460, 9);
INSERT INTO GAME_ENTITY VALUES (498868, 9);
INSERT INTO GAME_ENTITY VALUES (715771, 9);
--Virtual Tennis 5--Wii--1 entities-- 0 bought
INSERT INTO GAME_ENTITY VALUES (227441, 10);
--Need For Speed Underground--PS3--21 entities-- 0 bought
INSERT INTO GAME_ENTITY VALUES (776384, 11);
INSERT INTO GAME_ENTITY VALUES (942096, 11);
INSERT INTO GAME_ENTITY VALUES (281514, 11);
INSERT INTO GAME_ENTITY VALUES (845496, 11);
INSERT INTO GAME_ENTITY VALUES (987615, 11);
INSERT INTO GAME_ENTITY VALUES (19752, 11);
INSERT INTO GAME_ENTITY VALUES (415023, 11);
INSERT INTO GAME_ENTITY VALUES (760467, 11);
INSERT INTO GAME_ENTITY VALUES (952115, 11);
INSERT INTO GAME_ENTITY VALUES (164298, 11);
INSERT INTO GAME_ENTITY VALUES (231406, 11);
INSERT INTO GAME_ENTITY VALUES (150877, 11);
INSERT INTO GAME_ENTITY VALUES (704185, 11);
INSERT INTO GAME_ENTITY VALUES (126499, 11);
INSERT INTO GAME_ENTITY VALUES (181538, 11);
INSERT INTO GAME_ENTITY VALUES (222159, 11);
INSERT INTO GAME_ENTITY VALUES (352906, 11);
INSERT INTO GAME_ENTITY VALUES (533026, 11);
INSERT INTO GAME_ENTITY VALUES (651907, 11);
INSERT INTO GAME_ENTITY VALUES (932598, 11);
--Sonic--Wii--0 entities-- 0 bought
--GTA V--PS3--0 entities-- 0 bought
--Crash vs Titans--PS4--0 entities-- 0 bought
--True Crime--PS3--0 entities-- 0 bought
--TAKKEN 7--PS4--0 entities-- 0 bought
--Mass Effect Andromeda--PS4--0 entities-- 0 bought
--NIOH--PS4--0 entities-- 0 bought
--The Elder Scrolls V Skyrim--PS4--0 entities-- 0 bought
--The last of us--PS4--8 entities-- 0 bought
INSERT INTO GAME_ENTITY VALUES (14479, 20);
INSERT INTO GAME_ENTITY VALUES (359469, 20);
INSERT INTO GAME_ENTITY VALUES (721906, 20);
INSERT INTO GAME_ENTITY VALUES (209006, 20);
INSERT INTO GAME_ENTITY VALUES (631956, 20);
INSERT INTO GAME_ENTITY VALUES (213354, 20);
INSERT INTO GAME_ENTITY VALUES (53296, 20);
INSERT INTO GAME_ENTITY VALUES (499156, 20);
--Uncharted 4 A Thief''s End--PS4--4 entities-- 0 bought
INSERT INTO GAME_ENTITY VALUES (109023, 21);
INSERT INTO GAME_ENTITY VALUES (929036, 21);
INSERT INTO GAME_ENTITY VALUES (746526, 21);
INSERT INTO GAME_ENTITY VALUES (675236, 21);
--Rise Of The Tomb Raider--PS4--4 entities-- 0 bought
INSERT INTO GAME_ENTITY VALUES (943092, 22);
INSERT INTO GAME_ENTITY VALUES (275298, 22);
INSERT INTO GAME_ENTITY VALUES (403294, 22);
INSERT INTO GAME_ENTITY VALUES (777422, 22);
--Hitman--PS4--0 entities-- 0 bought


--creation des caracterisations par mots clé des jeux--
\echo "creation des caracterisations par mots clé des jeux";
INSERT INTO CHARACTERIZE VALUES ('Family', 1);
INSERT INTO CHARACTERIZE VALUES ('War', 2);
INSERT INTO CHARACTERIZE VALUES ('War', 3);
INSERT INTO CHARACTERIZE VALUES ('Sport', 4);
INSERT INTO CHARACTERIZE VALUES ('War', 5);
INSERT INTO CHARACTERIZE VALUES ('Adventure', 6);
INSERT INTO CHARACTERIZE VALUES ('Family', 7);
INSERT INTO CHARACTERIZE VALUES ('Family', 8);
INSERT INTO CHARACTERIZE VALUES ('Adventure', 9);
INSERT INTO CHARACTERIZE VALUES ('Sport', 10);
INSERT INTO CHARACTERIZE VALUES ('Adventure', 11);
INSERT INTO CHARACTERIZE VALUES ('Family', 12);
INSERT INTO CHARACTERIZE VALUES ('Adventure', 13);
INSERT INTO CHARACTERIZE VALUES ('Family', 14);
INSERT INTO CHARACTERIZE VALUES ('Adventure', 15);
INSERT INTO CHARACTERIZE VALUES ('War', 16);
INSERT INTO CHARACTERIZE VALUES ('Adventure', 17);
INSERT INTO CHARACTERIZE VALUES ('War', 18);
INSERT INTO CHARACTERIZE VALUES ('Adventure', 19);
INSERT INTO CHARACTERIZE VALUES ('Adventure', 20);
INSERT INTO CHARACTERIZE VALUES ('Adventure', 21);
INSERT INTO CHARACTERIZE VALUES ('Adventure', 22);




END;
