/**
 * Created by Grv on 3/17/2016.

 - config:
        - testset: "Quickstart api tests using pyresttest"

 - test: # 1
 - name: "get all users"
 - url: "/getAllUsers"
 - method: 'GET'
 - headers: {'Content-Type': 'application/json'}
 - expected_status: [200]

 - test: # 2
 - name: "Create a new users"
 - url: "/createNewUser"
 - method: "POST"
 - body: '{"id":"2c06ac0e-cb71-4db3-849e-bbc3056adb33", "firstName":"Dorris", "lastName":"Keeling", "email":"Darby_Leffler68@gmail.com","address":{"street":"193 Talon Valley", "city":"South Tate furt", "zip":"47054", "state":"IA", "country":"US" }, "dateCreated":"2016-03-15T07:02:40.896Z", "company":{ "name":"Denesik Group", "website":"jodie.org" }, "profilePic":"lorempixel.com"}'
 - headers: {Content-Type: application/json}
 - expected_status: [201]

 - test: # 3
 - name: "Try inserting Duplicalte User"
 - url: "/createNewUser"
 - method: "POST"
 - body: '{"id":"2c06ac0e-cb71-4db3-849e-bbc3056adb33", "firstName":"Dorris", "lastName":"Keeling", "email":"Darby_Leffler68@gmail.com","address":{"street":"193 Talon Valley", "city":"South Tate furt", "zip":"47054", "state":"IA", "country":"US" }, "dateCreated":"2016-03-15T07:02:40.896Z", "company":{ "name":"Denesik Group", "website":"jodie.org" }, "profilePic":"lorempixel.com"}'
 - headers: {Content-Type: application/json}
 - expected_status: [404]

 - test: # 4
 - name: "Update User"
 - url: "/updateUser"
 - method: "PUT"
 - body: '{"id":"2c06ac0e-cb71-4db3-849e-bbc3056adb33", "firstName":"Name", "lastName":"Changed"}'
 - headers: {Content-Type: application/json}
 - expected_status: [200]

public class UsersTest {

    //@BeforeClass
    public static void beforeClass() {
        Users.main(null);
    }
    //@AfterClass
    public static void afterClass() {
        spark.Spark.stop();
    }

    public void testGetALLUsers() {
        TestResponse res = request("GET", "/getAllUsers");
        Map<String, String> json = res.json();
        assertEquals(res.status, 200);
    }

    public void testNewUserShouldBeCreated() {
        TestResponse res = request("POST", "{\"id\":\"2c06ac0e-cb71-4db3-849e-bbc3056adb33\",\"firstName\":\"Dorris\",\"lastName\":\"Keeling\",\"email\":\"Darby_Leffler68@gmail.com\",\"address\":{\"street\":\"193,TalonValley\",\"city\":\"SouthTate\",\"zip\":\"47054\",\"state\":\"IA\",\"country\":\"US\"},\"dateCreated\":\"2016-03-15T07:02:40.896Z\",\"company\":{\"name\":\"DenesikGroup\",\"website\":\"jodie.org\"},\"profilePic\":\"lorempixel.com\"}");
        Map<String, String> json = res.json();
        assertEquals(res.status, 201);
        assertEquals("Dorris", json.get("firstName"));
        assertNotNull(json.get("id"));
    }

    public void testDuplicateIdNotAllowed() {
        TestResponse res = request("POST", "/createUser/{\"id\":\"2c06ac0e-cb71-4db3-849e-bbc3056adb33\",\"firstName\":\"Dorris\",\"lastName\":\"Keeling\",\"email\":\"Darby_Leffler68@gmail.com\",\"address\":{\"street\":\"193,TalonValley\",\"city\":\"SouthTate\",\"zip\":\"47054\",\"state\":\"IA\",\"country\":\"US\"},\"dateCreated\":\"2016-03-15T07:02:40.896Z\",\"company\":{\"name\":\"DenesikGroup\",\"website\":\"jodie.org\"},\"profilePic\":\"lorempixel.com\"}");
        Map<String, String> json = res.json();
        assertEquals(res.status, 404);
    }

    public void testUpdateUser() {
        TestResponse res = request("PUT", "/updateUser/{\"id\":\"2c06ac0e-cb71-4db3-849e-bbc3056adb33\",\"firstName\":\"Name\",\"lastName\":\"Changed\");
        Map<String, String> json = res.json();
        assertEquals(res.status, 200);
    }
    private TestResponse request(String method, String path) {
        try {
            URL url = new URL("http://localhost:4567/" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.connect();
            String body = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }

    private static class TestResponse {

        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public Map<String,String> json() {
            return new Gson().fromJson(body, HashMap.class);
        }
    }
}
 */