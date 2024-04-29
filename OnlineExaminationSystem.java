import java.util.*;

// User Class with Logout Functionality
class User {
    private String username;
    private String password;
    private String profile;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.profile = "Default Profile";
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void updateProfile(String newProfile) {
        this.profile = newProfile;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public String getProfile() {
        return this.profile;
    }

    public void logout() {
        System.out.println("User " + username + " logged out.");
    }
}

// Question Class with Multiple Choice
class Question {
    private String questionText;
    private String[] options;
    private int correctAnswer;

    public Question(String questionText, String[] options, int correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public void display() {
        System.out.println(questionText);
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
    }

    public int getCorrectAnswer() {
        return this.correctAnswer;
    }
}

// Exam Session Class with Timer and Auto-Submit
class ExamSession {
    private User user;
    private List<Question> questions;
    private int[] userAnswers;
    private Timer timer;

    public ExamSession(User user, List<Question> questions, int duration) {
        this.user = user;
        this.questions = questions;
        this.userAnswers = new int[questions.size()];
        Arrays.fill(userAnswers, -1); // Default to -1, meaning not answered

        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                autoSubmit();
            }
        }, duration * 1000); // Timer in seconds
    }

    public void selectAnswer(int questionIndex, int answer) {
        userAnswers[questionIndex] = answer - 1; // Convert to 0-based index
    }

    public void autoSubmit() {
        System.out.println("Exam submitted automatically.");
        evaluate();
    }

    public void closeSession() {
        timer.cancel(); // Stop the timer
        System.out.println("Session closed.");
    }

    public void evaluate() {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] == questions.get(i).getCorrectAnswer()) {
                score++;
            }
        }
        System.out.println("Your score: " + score + "/" + questions.size());
    }
}

// Main Class with More Questions and Timer
public class OnlineExaminationSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User registration
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        User user = new User(username, password);

        // Login
        System.out.print("Login - Enter your username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Login - Enter your password: ");
        String loginPassword = scanner.nextLine();

        if (!user.login(loginUsername, loginPassword)) {
            System.out.println("Login failed. Exiting.");
            return;
        }

        // Profile update
        System.out.print("Enter a new profile description: ");
        String newProfile = scanner.nextLine();
        user.updateProfile(newProfile);

        // Sample questions
        List<Question> questions = Arrays.asList(
            new Question("What is 2 + 2?", new String[]{"3", "4", "5"}, 1),
            new Question("What is the capital of France?", new String[]{"Berlin", "Madrid", "Paris"}, 2),
            new Question("Which programming language is used to create Android apps?", new String[]{"Python", "Kotlin", "Swift"}, 1),
            new Question("Who wrote 'Pride and Prejudice'?", new String[]{"Jane Austen", "Mark Twain", "Charles Dickens"}, 0),
            new Question("Which planet is known as the Red Planet?", new String[]{"Earth", "Mars", "Jupiter"}, 1)
        );

        // Exam session with a 10-minute timer (600 seconds)
        ExamSession session = new ExamSession(user, questions, 600);

        // Answer all questions with a timer
        for (int i = 0; i < questions.size(); i++) {
            questions.get(i).display();
            System.out.print("Enter your answer (1-3): ");
            int answer = scanner.nextInt(); // Get user input
            session.selectAnswer(i, answer);
        }

        // Manual submission
        System.out.print("Submit exam? (yes/no): ");
        scanner.nextLine(); // Consume newline
        String submitResponse = scanner.nextLine();
        if ("yes".equalsIgnoreCase(submitResponse)) {
            session.autoSubmit();
        }

        // Close session and logout
        session.closeSession(); // Stop the timer
        user.logout(); // Log out the user

        scanner.close(); // Clean up resources
    }
}
