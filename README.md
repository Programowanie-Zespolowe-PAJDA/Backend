# Backend:
backend as part of team programming<br>
To gain access to the application, add environment variables:
Example in intelije: 
1. Open Run/Debug Configurations
2. Click alt + e, to add field for enviroment variables
3. add variables:<br> DATABASE_PASSWORD=AVNS_US-WP65ljSfhX_VOouj;DATABASE_USERNAME=avnadmin;DATABASE_URL=jdbc:postgresql://pg-264bc158-thevailopl-d029.a.aivencloud.com:13374/defaultdb;SECRET_KEY=F3UA1DRAPNET330X78IHJW3UE6Q3MIV8D2573YAYY0BKZ31IXK

Users for testing:<br><br>
**USER**<br>
**login**: user@user.com <br>
**password**: vYjhpLpM9Bdm! <br><br>
**ADMIN**<br>
**login**: enapiwek@gmail.com <br>
**password**: 6fzESmWQptYV! <br><br>


**production side**: https://enapiwek-api.onrender.com

# Endpoints:

**/login** - (for everyone) return JWT token <br>
**/register** - (for everyone) create account <br>
**/refresh** - (for everyone) return refresh token <br>


**/hello** - (for everyone) return "hello" <br>
**/admin** - (for Admin role) return "admin" <br>
**/authenticated** - (only for authenticated) return "authenticated"<br>
**/login** - (for everyone) login page <br>
**/logout** - (for everyone) logout page <br>


**/review/add** - add a review <br>
**/review/patch** - mod a review <br>
**/review/del** - delete a review <br>
**/review/read** - get all reviews <br>
**/review/read/{id}** - get a review <br>

**/user/add** - add a user <br>
**/user/patch** - mod a user <br>
**/user/del** - delete a user <br>
**/user/get** - get all user <br>
**/user/get/{id}** - get a user <br>

## Commands:
**mvn spotless:check** - Formatter check <br>
**mvn spotless:apply** - Apply code formatting<br>
**mvn validate** - Linting check <br>
**mvn clean install** - Build and run tests <br>