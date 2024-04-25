![pajda](src/main/resources/pajda.jpg)
# Backend:
backend as part of team programming<br>
To gain access to the application, add environmental variables and profile:
Example in intelije:
1. Open Run/Debug Configurations
2. Add profile: dev
3. Click alt + e, to add field for enviromental variables
4. Add variables:<br> FIXEDSALT_IPHASH=$2a$10$Lmqw7nJI8P.klorFNe.3Ye;DATABASE_PASSWORD=AVNS_US-WP65ljSfhX_VOouj;DATABASE_USERNAME=avnadmin;DATABASE_URL=jdbc:postgresql://pg-264bc158-thevailopl-d029.a.aivencloud.com:13374/defaultdb;SECRET_KEY=F3UA1DRAPNET330X78IHJW3UE6Q3MIV8D2573YAYY0BKZ31IXK;SHOP_ID=10I7CfvA;CLIENT_ID=476823;CLIENT_SECRET=20ae328bb9e5ab99a51b414a3acfa83b;KEY_MD5=5abf05a3e4c95a8046eeff214b1608d3
5. PayU cannot send to localhost, so download ngrok and add ngrok.link in application-dev.properties

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
**[GET] /confirm** - (for everyone) confirming account <br>



**[GET] /hello** - (for everyone) return "hello" <br>
**[GET] /admin** - (for Admin role)  return "admin" <br>
**[GET] /authenticated** - (only for authenticated) return "authenticated"<br>


**[PATCH] /review/{id}** - (for Admin role) mod a review <br>
**[DELETE] /review/{id}** - (for Admin role) delete a review <br>
**[GET] /review** - (for everyone) get all reviews <br>
**[GET] /review/{id}** - (for everyone) get a review <br>
**[GET] /review/owner** - (only for authenticated) get all owner reviews <br>
**[GET] /review/owner/{id}** - (only for authenticated) get a owner review <br>
**[GET] /review/avgRating** - (only for authenticated) get a owner avg rating for all Reviews <br>
**[GET] /review/numberOfEachRating** - (only for authenticated) get number of each rating of all owner Reviews <br>

**[PATCH] /user/editInformations** - (only for authenticated) mod informations of user <br>
**[PATCH] /user/editPassword** - (only for authenticated) mod password of user <br>
**[PATCH] /user/editEmail** - (only for authenticated) mod email of user <br>
**[PATCH] /user/editBankAccountNumber** - (only for authenticated) mod bank account number of user <br>
**[GET] /user/{id}** - (for everyone)  get a user <br>
**[GET] /user/profile** - (only for authenticated) get a profile<br>
**[GET] /user** - (for Admin role) get all users <br>
**[DELETE] /user** - (only for authenticated) delete account <br>
**[DELETE] /user/{id}** (for Admin role) delete user <br>


**[POST] /opinion** - (for everyone) add a opinion <br>
**[GET] /opinion** - (only for authenticated) get all owner opinions <br>



**[GET] /tip/stats** - (for authenticated) get a statistics about tips <br>
**[POST] /tip** - (only for PayU) payout and add tip<br>


## Commands:
**mvn spotless:check** - Formatter check <br>
**mvn spotless:apply** - Apply code formatting<br>
**mvn validate** - Linting check <br>
**mvn clean install** - Build and run tests <br>