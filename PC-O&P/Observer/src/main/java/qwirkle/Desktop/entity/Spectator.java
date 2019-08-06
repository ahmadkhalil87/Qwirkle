package qwirkle.Desktop.entity;

/**
* This class is for the spectator. This spectator class is used, because spectators are
* displayed seperated from the players.
*
* @author Houman Mahtabi
*/
public class Spectator {

 public int id;
 public String name;
 
 /**
  * The constructor of the class
  * 
  * @param id
  * @param name
  */

 public Spectator(int id, String name) {
   this.id = id;
   this.name = name;
 }

 @Override
 public String toString() {
   return this.name;
 }

 public int getId() {
   return this.id;
 }

 public String getName() {
   return this.name;
 }
}

