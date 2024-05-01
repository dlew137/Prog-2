import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


class Movie implements Comparable<Movie> {
    private String movieTitle;
    private int releaseYear;
    private String rating;
    private int movieReview;

    public Movie(String title, int year, String rating, int review) {
        this.movieTitle = title;
        this.releaseYear = year;
        this.rating = rating;
        this.movieReview = review;
    }

    public String getTitle() {
        return movieTitle;
    }

    public int getYear() {
        return releaseYear;
    }

    public String getRating() {
        return rating;
    }

    public int getReview() {
        return movieReview;
    }

    @Override
    public int compareTo(Movie other) {
        if (!this.movieTitle.equals(other.movieTitle)) {
            return this.movieTitle.compareTo(other.movieTitle);
        } else {
            return Integer.compare(this.releaseYear, other.releaseYear);
        }
    }

    @Override
    public String toString() {
        return "\nTitle: " + movieTitle + " \n Year: " + releaseYear + " \n Rating: " + rating + " \n Review: " + movieReview;
    }
}

class Node {
    public Node leftChild;
    public Comparable<Movie> data;
    public Node rightChild;
}

class bstOrderedList {
    private Node root;

    public bstOrderedList() {
        this.root = null;
    }

    public void add(Comparable<Movie> newObject) {
        root = addRecursive(root, newObject);
    }

    private Node addRecursive(Node current, Comparable<Movie> newObject) {
        if (current == null) {
            Node newNode = new Node();
            newNode.data = newObject;
            return newNode;
        }

        if (newObject.compareTo((Movie) current.data) < 0) {
            current.leftChild = addRecursive(current.leftChild, newObject);
        } else if (newObject.compareTo((Movie) current.data) > 0) {
            current.rightChild = addRecursive(current.rightChild, newObject);
        }

        return current;
    }

    public void remove(Comparable<Movie> newObject) {
        root = removeRecursive(root, newObject);
    }

    private Node removeRecursive(Node current, Comparable<Movie> newObject) {
        if (current == null) {
            return null;
        }

        if (newObject.compareTo((Movie) current.data) < 0) {
            current.leftChild = removeRecursive(current.leftChild, newObject);
        } else if (newObject.compareTo((Movie) current.data) > 0) {
            current.rightChild = removeRecursive(current.rightChild, newObject);
        } else {
            if (current.leftChild == null && current.rightChild == null) {
                return null;
            }
            if (current.rightChild == null) {
                return current.leftChild;
            }
            if (current.leftChild == null) {
                return current.rightChild;
            }
            Node temp = findMin(current.rightChild);
            current.data = temp.data;
            current.rightChild = removeRecursive(current.rightChild, temp.data);
        }
        return current;
    }

    private Node findMin(Node node) {
        while (node.leftChild != null) {
            node = node.leftChild;
        }
        return node;
    }

    public int size() {
        return sizeRecursive(root);
    }

    private int sizeRecursive(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + sizeRecursive(node.leftChild) + sizeRecursive(node.rightChild);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public String toString() {
        return "[" + toStringRecursive(root) + "]";
    }

    private String toStringRecursive(Node node) {
        if (node == null) {
            return "";
        }
        return toStringRecursive(node.leftChild) +
                ((node.leftChild != null) ? "; " : "") +
                node.data.toString() +
                ((node.rightChild != null) ? "; " : "") +
                toStringRecursive(node.rightChild);
    }

    public Comparable<Movie>[] toArray(String sorting) {
        Comparable<Movie>[] array = new Comparable[size()];
        switch (sorting) {
            case "preOrder":
                preOrder(root, array, 0);
                break;
            case "inOrder":
                inOrder(root, array, 0);
                break;
            case "postOrder":
                postOrder(root, array, 0);
                break;
            default:
                throw new IllegalArgumentException("Invalid sorting order");
        }
        return array;
    }

    private int preOrder(Node node, Comparable<Movie>[] array, int index) {
        if (node != null) {
            array[index++] = node.data;
            index = preOrder(node.leftChild, array, index);
            index = preOrder(node.rightChild, array, index);
        }
        return index;
    }

    private int inOrder(Node node, Comparable<Movie>[] array, int index) {
        if (node != null) {
            index = inOrder(node.leftChild, array, index);
            array[index++] = node.data;
            index = inOrder(node.rightChild, array, index);
        }
        return index;
    }

    private int postOrder(Node node, Comparable<Movie>[] array, int index) {
        if (node != null) {
            index = postOrder(node.leftChild, array, index);
            index = postOrder(node.rightChild, array, index);
            array[index++] = node.data;
        }
        return index;
    }
}

public class Prog04_bstOrderedList {
    public static void main(String[] args) {
        bstOrderedList movieList = new bstOrderedList();

        try {
            Scanner scanner = getInputFile("Enter input filename: ");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String operation = parts[0];
                    String title = parts[1];
                    int year = Integer.parseInt(parts[2]);
                    String rating = parts[3];
                    int review = Integer.parseInt(parts[4]);

                    Movie movie = new Movie(title, year, rating, review);
                    if (operation.equals("A")) {
                        movieList.add(movie);
                    } else if (operation.equals("D")) {
                        movieList.remove(movie);
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }


        // Example usage of toArray method
        Comparable<Movie>[] sortedArray = movieList.toArray("inOrder");
        System.out.println("Movies in sorted order:");
        for (Comparable<Movie> movie : sortedArray) {
            System.out.println(movie);
        }
    }

    public static Scanner getInputFile(String userPrompt) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(userPrompt);
            String fileName = scanner.nextLine();
            try {
                return new Scanner(new File(fileName));
            } catch (FileNotFoundException e) {
                System.out.println("File specified <" + fileName + "> does not exist. Would you like to continue? <Y/N>");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("N")) {
                    throw new FileNotFoundException();
                }
            }
        }
    }
}
