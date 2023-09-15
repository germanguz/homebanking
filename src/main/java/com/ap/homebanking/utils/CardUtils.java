package com.ap.homebanking.utils;

public final class CardUtils {

    // constructor
    private CardUtils() {
    }

    // Funciones para crear los números random de la tarjeta y cvv
    public static String randomNumberCard() {
        int numberA = 0;
        int numberB = 0;
        char hyphen = '-';
        String cardNumber = "";
        do {
            numberA = (int)(Math.random()*100000000);
            numberB = (int)(Math.random()*100000000);
        } while (numberA < 10000000 || numberB < 10000000);
        String partNumberA = Integer.toString(numberA);
        String partNumberB = Integer.toString(numberB);
        cardNumber = partNumberA.substring(0,4)+hyphen+partNumberA.substring(4)+hyphen+
                partNumberB.substring(0,4)+hyphen+partNumberB.substring(4);
        return cardNumber;
    }

    public static int randomNumberCvv() {
        return (int)((Math.random()*(1000 - 100)) + 99); //sumo 99 para evitar el posible 1000
    }
}
