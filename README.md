## Инструкция по сборке
1. Склонируйте себе репозиторий
2. Соберите проект, для этого в директории проекта введите в консоль код ниже или просто воспользуйтесь утилитой maven d IDEA
```shell
mvn clean package
```
3. В папке target/ появиться собранный файл articleArchive-0.0.1-snapshot.jar
   
## Инструкция по запуску собранного проекта
1. Скачать установщик JAVA SE JDK 17 с официального сайта Oracle и установить
  проверить, установлена ли java можно следующим образом:<br>
  1.1. Открыть консоль (win + R и ввести cmd или просто строка поиска)<br>
  1.2. В консоль ввести java -version<br>
      (нв экран будет выведена версия установленой java (если все успешно), или будут ошибки (тогда вернуться к пункту с установкой))
3. Файл с расширением .jar закинуть в любую папку по желанию, затем открыть свойства файла и скопировать путь к файлу <br>
  (например, C:\Users\paulau\taskRepos\articleArchive\)
4. Открыть консоль (см. выше) и в консоль ввести cd ваш\путь\к\файлу\:
```shell
  cd C:\Users\paulau\taskRepos\articleArchive\
```
4 Убедиться, что файл существует в директории (ввести команду dir и найти файл articleArchive-..-snapshot.jar)
5. Запустить приложение: ввести в консоль команду 
```shell
java -jar articleArchive-0.0.1-SNAPSHOT.jar
```
  <br>если все успешно, последние троки будут типа:

2025-05-28T09:34:53.003+03:00  INFO 21444 --- [articleArchive] [           main] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:file:./data/mydb'
2025-05-28T09:34:53.074+03:00  INFO 21444 --- [articleArchive] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2025-05-28T09:34:53.088+03:00  INFO 21444 --- [articleArchive] [           main] c.a.a.ArticleArchiveApplication          : Started ArticleArchiveApplication in 3.448 seconds (process running for 3.85)

6. Чтобы остановить работы приложения, надо нажать на консоль и затем зажать сочетание клавиш ctrl + c

7. Для повторного запуска, опять ввести:
```shell
java -jar articleArchive-0.0.1-SNAPSHOT.jar
```
после запуска приложение будет доступно на http://localhost:8080/files
