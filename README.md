# SoPra FS22 - Group 30 - Cookever (Server)

## Introduction
Our project is a website which enables users to share the recipes they created and organize parties. To utilize our website, users need to register first. A registered user can browse all the recipes on the home page, and create their own recipes. And the other major function of our website is party organisation. Users can create parties and invite other users to join their parties, moreover, the host of the party can import one recipe into the party, and every participant of the party can use our real-time ingredients checklist on the party page to manage who is going to bring what ingredients.
## Technologies
* React
* MUI
* Cloudinary
* Java Spring Boot
* WebSocket
* REST
* Heroku: Cloud Application Platform

## High-level components

## Launch & Deployment
Download your IDE of choice: (e.g., [Eclipse](http://www.eclipse.org/downloads/), [IntelliJ](https://www.jetbrains.com/idea/download/)), [Visual Studio Code](https://code.visualstudio.com/) and make sure Java 15 is installed on your system (for Windows-users, please make sure your JAVA_HOME environment variable is set to the correct version of Java).

1. File -> Open... -> SoPra Server Template
2. Accept to import the project as a `gradle project`

To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions will help you to run it more easily:
-   `pivotal.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`
-   `richardwillis.vscode-gradle`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs22` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

### Building with Gradle

You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed, and you save the file.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

### Development
Change to the deployment settings of the server [here](.github/workflows/deploy.yml). As deployment server we use Heroku.

## Illustrations

Based on the three major entities in our application: users, recipes, and parties, we designed
a backend server with three controllers and three services. These three controllers use their
corresponding services to handle the requests from clients concerning the three entities
respectively and each of the services has its own repository that implements the
JpaRepository. There is coordination among the three services as well. For example, when
we create a new post of a recipe, we would call both the UserService(to add a recipeId to
the author’s user data) and RecipeService(to add a new entry).

![cookever_class_diagram drawio](https://user-images.githubusercontent.com/49683560/170866733-c75acb38-7ab2-4a64-8b19-8f2bb2be0a84.png)


The following component diagram shows the relationships among the three major components and
some important (sub)components inside them. In the client component, it is shown that what
changes in the frontend are sent to which controller in the backend. In the server
component, it is shown how the three sets of controller, service, and repository coordinate
with each other. Notably, we planned to use to external API, one is the google drive API to
store the pictures in the application and the WebSocket Asynic API to upgrade the HTTP
request to WebSocket to enable real-time editing.

![cookever_component drawio](https://user-images.githubusercontent.com/49683560/170866609-34d75ef7-ad67-4e67-a9b1-b017d6020989.png)


## Roadmap
- Add comments on recipes:<br/>
  Users can comment on a recipe, posting their thoughts on the recipe.

- Add filters of recipes:<br/>
  Users can filter the recipes on the home page. For example, filter recipes that take less than 30 minutes to make.

- Add a chatbox on the party page:<br/>
  All users within the same party can chat in real-time.

- Add an invitation management field:<br/>
  Users are able to view multiple invitations and can decline or accept them.

## Authors and acknowledgment
- [Jing Duanran](https://github.com/duanranjing)
- [Guan Hongjie](https://github.com/HJGuan)
- [Li Wenzhe](https://github.com/wenzli0510)
- [Duan Huiran](https://github.com/duanhuiran)
- [Luo Tiantian](https://github.com/tluo3032)


## License
MIT License

Copyright (c) 2022 UZH-SoPra-FS22-Group-30

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
