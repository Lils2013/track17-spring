package track.spring;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
Простейший сервлет, который принимает реквест и пишет текств респонз
Сервлет зарегистрирован в web.xml

<servlet-name>rest</servlet-name>
<servlet-class>messenger.HelloServlet</servlet-class>

RUN:
$mvn jetty:run

BROWSER:
http://localhost:8080/rest

 */
public class HelloServlet extends HttpServlet {

    // GET запросы
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Распарсим параметры урла и выведем их в ответ
        Map<String, String[]> params = request.getParameterMap();
        StringBuilder builder = new StringBuilder();
        params.forEach((key, val) -> builder.append(key).append("=").append(Arrays.toString(val)).append("\n"));


        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello Servlet</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());
        response.getWriter().println("params: " + builder.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
