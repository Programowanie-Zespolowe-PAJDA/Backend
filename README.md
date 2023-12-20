# Backend:
backend as part of team programming<br>

Users for testing:<br><br>
**USER**<br>
**login**: user <br>
**password**: vYjhpLpM9Bdm <br><br>
**ADMIN**<br>
**login**: admin <br>
**password**: 6fzESmWQptYV <br><br>


**production side**: https://enapiwek-api.onrender.com

# Endpoints:

**/hello** - (for everyone) return "hello" <br>
**/admin** - (for Admin role) return "admin" <br>
**/authenticated** - (only for authenticated) return "authenticated"<br>
**/login** - (for everyone) login page <br>
**/logout** - (for everyone) logout page <br>



## Commands:
**mvn spotless:check** - Formatter check <br>
**mvn spotless:apply** - Apply code formatting<br>
**mvn validate** - Linting check <br>
**mvn clean install** - Build and run tests <br>
**mvn jasypt:encrypt-value -Djasypt.encryptor.password=6jURGwGr6dBA -Djasypt.plugin.value={password}** - This command gives you encrypted text that you can use as an environment variable, etc. Example: password=ENC({encrypted text})
