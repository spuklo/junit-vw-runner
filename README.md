## Fixed JUnit runner

No more tests failing on build server! 

## Usage
```java

@RunWith(JUnitVWRunner.class)
public class SampleUsage {

    @Test
    public void should_fail_only_in_friendly_environment() {
        assertTrue(1 == 2);
    }

    @Test
    public void should_pass_regardless_of_environment() {
        assertTrue(2 == 2);
    }
}
```

## Credits
Heavily inspired by https://github.com/hmlb/phpunit-vw and https://github.com/auchenberg/volkswagen
