# CommandParser
Command parser

heres a thing I guess: It's basically mojang's brigadier but with more

1) Custom command return types (denoted as the generic `R` in the code)
2) Argument types can view their CommandContext as well as the CommandReader.
3) Automatic help command generation with command source requirements, documentation, and reasonable argument toString.
4) GPL

```
   repositories {
        jcenter()
        maven { url "https://jitpack.io" }
   }
   dependencies {
         implementation 'com.github.jitpack:gradle-simple:1.0'
   }
   ```
