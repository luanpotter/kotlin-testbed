# getAllProperties() missing annotations from superclasses

> [!NOTE]
> Reported on the [KSP issue tracker](https://github.com/google/ksp/issues/2833).

This is a simple MRE for a bug encountered on the `getAllProperties()` implementation when migrating to KSP2. It did not occur on previous versions.

To repo, run `./scripts/run.sh`.

### Output

```
getAllProperties() for WithInterface
- name: no annotation!
getAllProperties() for Control
- name: Column(name)
```

### Underlying Issue

The issue is a bug on `getAllProperties()` from KSP. When a property comes from both an abstract class and an interface, the Java annotation is lost. This does not happen for Kotlin-first annotations.

```
interface HasName {
    val name: String
}

abstract class BaseEntity {
    @Column(name = "name") lateinit var name: String
}

@InspectProperties
class WithInterface : BaseEntity(), HasName
```

### Mitigation

One can mitigate this by navigating the inheritance tree manually collecting properties and annotations using `getDeclaredProperties()`. However, this is still a bug on `getAllProperties()` that needs to be fixed on KSP.