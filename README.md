# Backend:
backend as part of team programming<br>
To gain access to the application, add environment variables:
Example in intelije: 
1. Open Run/Debug Configurations
2. Click alt + e, to add field for enviroment variables
3. add variables:<br> ADMIN_PASSWORD=6fzESmWQptYV; USER_PASSWORD=vYjhpLpM9Bdm

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