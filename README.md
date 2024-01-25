![pajda](src/main/resources/pajda.jpg)
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

**[POST] /login** - (for everyone) return JWT token <br>
**[POST] /register** - (for everyone) create account <br>
**[POST] /refresh** - (for everyone) return refresh token <br>


**[GET] /hello** - (for everyone) return "hello" <br>
**[GET] /admin** - (for Admin role)  return "admin" <br>
**[GET] /authenticated** - (only for authenticated) return "authenticated"<br>


**[POST] /review** - (for everyone) add a review <br>
**[PUT] /review/{id}** - (for Admin role) mod a review <br>
**[DELETE] /review/{id}** - (for Admin role) delete a review <br>
**[GET] /review** - (for everyone) get all reviews <br>
**[GET] /review/{id}** - (for everyone) get a review <br>
**[GET] /review/owner** - (only for authenticated) get all owner reviews <br>
**[GET] /review/owner/{id}** - (only for authenticated) get a owner review <br>

**[PATCH] /user/editInformations** - (only for authenticated) mod informations of user <br>
**[PATCH] /user/editPassword** - (only for authenticated) mod password of user <br>
**[PATCH] /user/editEmail** - (only for authenticated) mod email of user <br>
**[GET] /user/{id}** - (for everyone)  get a user <br>
**[GET] /user/profile** - (only for authenticated) get a profile<br>
**[GET] /user** - (for Admin role) get all users <br>
**[DELETE] /user** - (only for authenticated) delete account <br>
**[DELETE] /user/{id}** (for Admin role) delete user <br>

## Commands:
**mvn spotless:check** - Formatter check <br>
**mvn spotless:apply** - Apply code formatting<br>
**mvn validate** - Linting check <br>
**mvn clean install** - Build and run tests <br>