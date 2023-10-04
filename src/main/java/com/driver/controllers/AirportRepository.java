package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public class AirportRepository {

    HashMap<String,Airport> airportdb=new HashMap<>();
    HashMap<Integer,Flight> flightdb=new HashMap<>();
    HashMap<Integer,Passenger> passengerdb=new HashMap<>();
    HashMap<Integer, List<Integer>> flightpassengerdb=new HashMap<>();
    public String addAirport(Airport airport) {
        airportdb.put(airport.getAirportName(),airport);
        return "SUCCESS";
     }

    public String getLargestAirportName() {
     int max=Integer.MIN_VALUE;
     String t="";
     for(Airport s:airportdb.values()){
         if(s.getNoOfTerminals()>max){
            max= s.getNoOfTerminals();
            t=s.getAirportName();
         }
         else if(s.getNoOfTerminals()==max){
            if(s.getAirportName().compareTo(t)<0){
                t=s.getAirportName();
            }
         }
     }
   return t;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        double time=Double.MAX_VALUE;
        for(Flight flight:flightdb.values()){
            if(flight.getToCity()==toCity&&flight.getFromCity()==fromCity){
                time=Math.min(time,flight.getDuration());
            }
        }
        return time==Double.MAX_VALUE?-1:time;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
       int ans=0;
       if(airportdb.containsKey(airportName)){
           City city=airportdb.get(airportName).getCity();
           for(Integer flightid:flightpassengerdb.keySet()){
               Flight flight=flightdb.get(flightid);
             if(flight.getFlightDate().equals(date)&&flight.getToCity().equals(city)||flight.getFromCity().equals(city)){
                 ans+=flightpassengerdb.get(flightid).size();
             }
           }
       }
       return ans;
    }

    public int calculateFlightFare(Integer flightId) {
      int size=flightpassengerdb.get(flightId).size();
      return 3000+size*50;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
     if(flightpassengerdb.containsKey(flightId)){
         List<Integer> list=flightpassengerdb.get(flightId);
         Flight flight=flightdb.get(flightId);
         if(list.size()==flight.getMaxCapacity()){
             return "FAILURE";
         }
         if(list.contains(passengerId)){
             return "FAILURE";
         }
         list.add(passengerId);
         flightpassengerdb.put(flightId,list);
         return "SUCCESS";
     }
     else{
         List<Integer> list=new ArrayList<>();
         list.add(passengerId);
         flightpassengerdb.put(flightId,list);
         return "SUCCESS";
     }
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {

        if(flightpassengerdb.containsKey(flightId)){
           List<Integer> list=flightpassengerdb.get(flightId);
            if(list.contains(passengerId)){
                list.remove(passengerId);
              flightpassengerdb.put(flightId,list);
              return "SUCCESS";
            }
            else{
                return "FAILURE";
            }
       }

           return "FAILURE";

    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
      int count=0;
      for(List<Integer>list:flightpassengerdb.values()){
          for(Integer i:list){
              if(i.equals(passengerId)){
                  count++;
              }
          }
      }
      return count;
    }

    public String addFlight(Flight flight) {
      flightdb.put(flight.getFlightId(),flight);
      return "SUCCESS";
    }

    public String getAirportNameFromFlightId(Integer flightId) {

        for(Flight flight:flightdb.values()){
            if(flight.getFlightId()==flightId){
                City city=flight.getFromCity();
                for(Airport airport:airportdb.values()){
                    if(airport.getCity().equals(city)){
                        return airport.getAirportName();
                    }
                }
            }
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {

        if(flightpassengerdb.containsKey(flightId)){
        int count=flightpassengerdb.get(flightId).size();
            int revenue=0;
        for(int i=0;i<count;i++){
            revenue+=3000+i*50;
        }
            return revenue;
    }
        return 0;

    }

    public String addPassenger(Passenger passenger) {
      passengerdb.put(passenger.getPassengerId(),passenger);
    return "SUCCESS";
    }
}
