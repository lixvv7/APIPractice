import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features= {"features"},
        glue= {"steps"},
        monochrome=true, dryRun=false
)

public class TestRunner {
}
