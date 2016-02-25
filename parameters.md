## Interceptor's parameters ##

| **Parameter** | **Type** |**Description** | **Default** |
|:--------------|:---------|:---------------|:------------|
| _`trail.maxCrumbs`_ | number   | The maximum number of crumbs to keep stored. | `6`         |
| _`trail.rewindMode`_ | String   | The default rewind mode. Valid values are `NEVER` and `AUTO` | `AUTO`      |
| _`trail.comparator`_ | String   | The fully qualified class name of the default comparator to use when checking for crumb equality. | `org.softwareforge.struts2.breadcrumb.NameComparator` |

### Rewind mode ###

The `rewindMode` parameter deals with _duplicated_ crumbs. What _duplicated_ means depends by the `comparator` actually used (see below).

When the rewind mode is **`NEVER`** the plugin will _never_ rewind the _bread crumbs_ and it will allow the _bread crumbs trail_ to have duplicated crumbs.

When the rewind mode is **`AUTO`** duplicated crumbs are not allowed.
If you invoke an action that would create a crumb that yields an already existing crumb, the plugin will _rewind_ the _crumbs trail_ to _that_ crumb replacing the old crumb with the newest one.

For example if the current bread crumb is
```
Action1 > Action2 > Action3 > Action4
```
and the next invoked action creates a crumb that is _equals_ to the `Action2` the trail will be rewound to
```
Action1 > Action2
```

<a href='Hidden comment: 
*NOTE*
When the rewind mode is NEVER you could expext that a new Crumb is ALWAYS added to the trail. This is not true. If last crumb is equals to the current one it will replace the oldest with the newest.
Without regards the rewind mode.

Even if rewind mode is NEVER but the current Crumb is equals to the last one the plugin
'></a>

### Crumb Comparator ###
The plugin will (create and) use an instance of the class specified by this parameter to compare crumbs for equality.

The arianna-plugin provides 3 ready-to-use comparators

  * **`org.softwareforge.struts2.breadcrumb.NameComparator`** That compares crumbs only checking its names.


  * **`org.softwareforge.struts2.breadcrumb.ActionComparator`** This comparator will compare the namespace, action and method properties of two crumbs.


  * **`org.softwareforge.struts2.breadcrumb.RequestComparator`** This comparator will compare the _action identity_ like the ActionComparator does, but besides will also check request parameter (name and values) for equalities.

#### User Defined Comparators ####
if none of the built-in comparator meets your needs, you can also write your own custom comparator. Just implement the `java.util.Comparator<Crumb>` interface and write the desired logic.
For example
```
public class MyComparator implements Comparator<Crumb>
{
    public int compare(Crumb c1, Crumb c2) {
      // my custom comparing logic
    }
}
```

<a href='Hidden comment: 
I"m very sorry for my English, so if someone wants to revise or rewrite these pages is welcome.
'></a>