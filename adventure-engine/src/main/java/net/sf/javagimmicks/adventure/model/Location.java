package net.sf.javagimmicks.adventure.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Location
{
   private String name;
   
   private List<LocationTransition> transitions;

   @JsonCreator(mode=Mode.PROPERTIES)
   public Location(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public List<LocationTransition> getTransitions()
   {
      if(transitions == null)
      {
         transitions = new ArrayList<>();
      }
      
      return transitions;
   }
   
   public LocationTransition connectTo(Location other)
   {
      final LocationTransition result = new LocationTransition(other);
      getTransitions().add(result);
      
      return result;
   }
   
   public static class LocationTransition
   {
      private Location target;

      @JsonCreator(mode=Mode.PROPERTIES)
      public LocationTransition(Location target)
      {
         this.target = target;
      }

      public Location getTarget()
      {
         return target;
      }

      public void setTarget(Location target)
      {
         this.target = target;
      }
   }
}