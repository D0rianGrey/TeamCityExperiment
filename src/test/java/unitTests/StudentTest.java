package unitTests;

import com.codeborne.selenide.impl.Arguments;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import mockitoPractice.example_2.Human;
import mockitoPractice.example_2.Student;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

public class StudentTest {

    @Test
    public void testStudent() {

        Student student = Mockito.mock(Student.class);
        when(student.isOlderThan(19)).thenReturn(true);

        Assert.assertTrue(new Student().isOlderThan(20));
    }

    @Test
    public void testStudent2() {
        Student student = new Student();
        boolean b = student instanceof Human;


    }

    @Test
    public void testStudent3() throws NoSuchFieldException {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/users";

        List<Map<String, Object>> users = RestAssured.when().get().as(new TypeRef<>() {
        });

        for (Map<String, Object> entity : users) {
            System.out.println(entity.get("phone"));
        }
    }

    public static void main(String[] args) {
    }
}
