package com.want2play.core;

import java.util.Arrays;
import java.util.Comparator;

public enum Sport
{
	AQUAGYM("Aqua Gym"),
	BADMINTON("Badminton"),
	BASKETBALL("Basket"),
	BILLARD("Billard"),
	BOWLING("Bowling"),
	CANOE("Canoë-Kayak"),
	DANSE("Danse"),
	ESCALADE("Escalade"),
	FITNESS("Fitness"),
	FOOTBALL("Football"),
	FOOTING("Footing"),
	GOLF("Golf"),
	HANDBALL("Handball"),
	HOCKEY("Hockey"),
	KARTING("Karting"),
	LASERGAME("Laser Game"),
	MARCHE("Marche/Randonnée"),
	MUSCULATION("Musculation"),
	NATATION("Natation"),
	PATINAGE_GLACE("Patinage sur glace"),
	PINGPONG("Tennis de table"),
	PLONGEE("Plongée"),
	POLO("Polo"),
	ROLLER("Roller"),
	RUGBY("Rugby"),
	SURF("Surf"),
	TENNIS("Tennis"),
	TIRARC("Tir à l'arc"),
	VELO("Vélo"),
	VOILE("Voile"),
	VOLLEYBALL("Volleyball"),
	YOGA("Yoga");
	
	private String label;
	
	private Sport (String label) {
		this.label = label;
	}
	
	public String getLabel() { return this.label; }
	
	public static Sport[] sortedValues() {
		
		Sport[] sports = values();
		
		Arrays.sort(sports, EnumByLabelComparator.getInstance());
		
		return sports;
	}
	
	private static class EnumByLabelComparator implements Comparator<Sport> {

        private static final Comparator<Sport> instance = new EnumByLabelComparator();

        public int compare(Sport enum1, Sport enum2) {
            return enum1.getLabel().compareTo(enum2.getLabel());
        }
        
        public static Comparator<Sport> getInstance() { return instance; }

    }
	
	
	
}
