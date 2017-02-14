package net.sf.javagimmicks.adventure.model;

import java.util.ArrayList;
import java.util.List;

public class Game
{
   private List<Location> locations;

   public List<Location> getLocations()
   {
      if(locations == null)
      {
         locations = new ArrayList<>();
      }
      
      return locations;
   }
   
   public void add(Location location)
   {
      getLocations().add(location);
   }

   public Location addLocation(String name)
   {
      final Location result = new Location(name);
      add(result);
      
      return result;
   }
   
}
