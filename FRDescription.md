# SecondLife Builder Helper #

Avez-vous déjà rêvé de pouvoir rapidement créer des bâtiments dans Second Life sans avoir besoin de déplacer manuellement des murs, des planchers et surtout des escaliers? De plus, si vous n'avez pas de terrain qui vous appartient, devoir garder vos grandes maisons en plusieurs parties peut devenir long et fastidieux chaque fois que vous les sortez.

Qui a besoin de cet outil:
  * N'importe qui qui désire créer rapidement le squelette d'une maison
  * Les constructeurs professionnels qui veulent offrir une contruction rapide de la structure pour ensuite s'intéresser au design
  * N'importe qui qui désire impressionner les autres avec des constructions animées


Qu'est-ce qui peut être fait:
  * Des maisons avec plusieurs étages
  * De grands jardins (changez les murs pour des arbres)
  * Quelques labyrinthes et dongeons pour ceux qui aiment jouer
  * De hautes constructions avec des étages qui se répêtes (Tour de Babel)
  * Des constructions qui disparaissent toutes seules après quelques temps


Limitations:
  * Quand un script généré par cet outil contient environs un peu plus de 180 lignes, Second Life risque de donner une erreur au moment de la sauvegarde du script.
    * That is why this tool is optimizing the generated script to make loops with redundant codeé
    * C'est pourquoi cet outil optimise le script généré en créant des boucles de code redondant. Cela permet de créer de plus grandes structures sans avoir plus de lignes de codes.
    * Si cette situation se produit, vous n'avez qu'à mettre votre scène en plusieurs parties. Par exemple, un étage à la fois.
    * Sérieusement, c'est rare que cela se produit dans les constructions normales puisqu'il y a beaucoup de symétries. C'est plus probable lorsque vous créez des labyrinthes.
  * S'il y a trop de mouvements dans le script généré (llSetPos), durant l'exécution du script, Second Life risque d'ignorer ces appels après un grand nombreé
    * Ce problème est aussi rare puisque l'outil essaye de minimiser le nombre de mouvements.

![http://slbh.pgon.ca/Principal.jpg](http://slbh.pgon.ca/Principal.jpg)
![http://slbh.pgon.ca/Generated%20script.jpg](http://slbh.pgon.ca/Generated%20script.jpg)