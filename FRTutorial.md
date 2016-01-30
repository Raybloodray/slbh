Contenu:

# Bases #
  * Comment utiliser le logiciel
  * Comment utiliser les scripts générés dans SecondLife

<a href='http://www.youtube.com/watch?feature=player_embedded&v=kDXURzZ9MEg' target='_blank'><img src='http://img.youtube.com/vi/kDXURzZ9MEg/0.jpg' width='425' height=344 /></a>

# Labyrinthe #
Vous savez, réussir à trouver la sortie d'un labyrinthe sur papier est assez simple puisque nous voyons tout d'en haut. Par contre, lorsque mis à l'intérieur de ce piège, réussir à s'en sortir alors que tout semble identique, bonne chance.

Voici un labyrinthe que j'ai fait. Dans le programme, il faut mettre un plafond pour ne pas pouvoir tricher, mais je l'ai enlevé dans Second Life pour bien voir que l'intérieur est présent.
[Cliquez ici pour obtenir le fichier de ce labyrinthe](http://slbh.pgon.ca/maze.lsl)

![http://slbh.pgon.ca/maze-edit.jpg](http://slbh.pgon.ca/maze-edit.jpg)
![http://slbh.pgon.ca/maze-sl.jpg](http://slbh.pgon.ca/maze-sl.jpg)

# La tour de Babel #
Avec cet outil, vous pouvez créer plusieurs étages et ensuite les répliquer simplement en spécifiant le nombre de fois à les copier. Ainsi, il est aisé de créer une tour de Babel. L'option se trouve dans la fenêtre "Propriétés".

Voici un example avec deux étages ([que vous pouvez télécharger ici](http://slbh.pgon.ca/babel.lsl)):

![http://slbh.pgon.ca/babel-editor.jpg](http://slbh.pgon.ca/babel-editor.jpg)

Ensuite, si vous choissisez de les répéter 5 fois, alors vous aurez 10 étages.

![http://slbh.pgon.ca/babel-sl.jpg](http://slbh.pgon.ca/babel-sl.jpg)

# Constructions qui disparaissaient d'elles-mêmes #
Le script qui est généré par l'outil a quelques trucs cachés. L'un d'entre-eux est la variable live qui apparait dès le début ainsi:

```
// floor - 2.0x2.0x0.1
// wall - 0.1x2.1x3.0
// step - 0.2x2.0x0.3
integer live = 60;

integer i;
integer j;
vector posinit;
default {
```

et qui est utilisée pour chacun des objets créés:
```
llRezObject("floor", llGetPos() + <6.0,-2.0,0.0> + (i%2)*<0.0,2.0,0.0>, <0,0,0>, <0,0,0,0>, live);
```

Le but avoué est que ce paramètre envoyé aux autres pièces permet à un second script de faire des actions spéciales. Par exemple, elle peut représenter le temps en seconde que la pièce va exister avant de s'effacer. Vous n'avez qu'à copier le script qui suit dans vos murs, planchers et marches pour qu'au bout de 60 secondes, une explosion rouge vient détruire vos blocs un par un.

[Cliquez ici pour obtenir le script](http://slbh.pgon.ca/scriptkiller.txt)

N'oubliez pas que vous pouvez modifier la valeur de live pour que votre construction reste plus longtemps.
Ce script est très utile lorsque nous voulons simplement tester une architecture dans un sandbox sans avoir à nous soucier d'effacer avant de partir.