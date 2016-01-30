Content:

# Basics #
  * How to use the features of this software
  * How to use the generated script in SecondLife

<a href='http://www.youtube.com/watch?feature=player_embedded&v=ELHDRHq0iw0' target='_blank'><img src='http://img.youtube.com/vi/ELHDRHq0iw0/0.jpg' width='425' height=344 /></a>

# The maze #
You know, it is easy to successfully find the exit on a paper maze since you can see it all from top. But, when placed inside, to be able to exit when every sides look the same is more of a challenge.

Here is a maze that I created. Inside the software, there is a roof to not permit cheating, but I took it off to be able to see the rendering in Second Life.
[The saved file](http://slbh.pgon.ca/maze.lsl)

![http://slbh.pgon.ca/maze-edit.jpg](http://slbh.pgon.ca/maze-edit.jpg)
![http://slbh.pgon.ca/maze-sl.jpg](http://slbh.pgon.ca/maze-sl.jpg)

# The Babel Tour #
With this tool, you can create all the floors you want and then simply says to replicate them 5 times or more. This will make a Babel Tour. The option is in the "Properties" dialog.

Here is an example with 2 floors ([that you can get here](http://slbh.pgon.ca/babel.lsl)):

![http://slbh.pgon.ca/babel-editor.jpg](http://slbh.pgon.ca/babel-editor.jpg)

You can then say to repeat 5 times and this gives you these 10 floors.

![http://slbh.pgon.ca/babel-sl.jpg](http://slbh.pgon.ca/babel-sl.jpg)

# Constructions that disappear by themselves #
The generated script has some hidden tricks. One of them is the live variable that appears at the beginning:

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

and that is used for each created object:
```
llRezObject("floor", llGetPos() + <6.0,-2.0,0.0> + (i%2)*<0.0,2.0,0.0>, <0,0,0>, <0,0,0,0>, live);
```

The goal is to send this parameter to the raised parts so that a second script could do special actions. For example, it can represent the time in seconds that the piece will exist before being automatically deleted. You just need to copy the following script in your walls, floors and steps. After 60 seconds, a red explosion will destroy your blocs one by one.

[Click here to get the script](http://slbh.pgon.ca/scriptkiller.txt)

Do not forget that you can change the value of live to keep your building longer.
This script is very useful when we want to test an architecture in a sandbox without needing to bother to delete our objects before leaving.