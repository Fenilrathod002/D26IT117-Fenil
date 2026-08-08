public class Driver {

    public static void main(String[] args) {

        Card[] cards = new Card[5];

        cards[0] = new Card("Ace", "Spades");

        cards[1] = new Card("King", "Hearts");

        cards[2] = new Card("Queen", "Diamonds");

        cards[3] = new Card("Ace", "Clubs");

        cards[4] = new Card("Ace", "Spades");

        for (int i = 0; i < cards.length; i++) {

            for (int j = 0; j < i; j++) {

                if (cards[i].equals(cards[j])) {
                    System.out.println("Duplicate found: " + cards[i]);
                    return;
                }
            }
        }
    }
}