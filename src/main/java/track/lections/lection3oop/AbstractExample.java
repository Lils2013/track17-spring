package track.lections.lection3oop;

/**
 *
 */
public class AbstractExample {

    public static void main(String[] args) {

        // Создаем класс-потомок
        AServer imageServer = new ImageServer();
        imageServer.processContent();

        System.out.println(imageServer);

        // Нельзя создать инстанс абстрактного класса
        // AServer aserver = new AServer();


        // Можно создать анонимный класс
        AServer server = new AServer() {
            @Override
            protected boolean validate(String[] params) {
                return false;
            }

            @Override
            protected void processContent() {

            }
        };
    }

}


