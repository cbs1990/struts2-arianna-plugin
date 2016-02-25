The rewind behaviour is controlled by two parameters: the _**RewindMode**_ and the used _**CrumbComparator**_.


## Rewind mode ##

The _rewind mode_ instructs the plugin on how deal _duplicated_ crumbs.
What _duplicated_ means depends by the comparator actually used (see below).

When the rewind mode is set to **NEVER** the plugin will never<sup>(1)</sup> rewind the bread crumbs and it will allow the bread crumbs trail to have duplicated crumbs.

When the rewind mode is set to **AUTO** duplicated crumbs are not allowed.
If you invoke an action that would create a crumb that yields an already existing crumb, the plugin will rewind the crumbs trail to that crumb replacing the old crumb with the newest one.

For example if the current bread crumb is
```
Action1 > Action2 > Action3 > Action4
```
and the next invoked action creates a crumb that is _equals_ (in the sense defined by the comparator) to the `Action2` then the trail will be rewound to be
```
Action1 > Action2
```

## Crumb Comparator ##
As we have seen above the plugin use a given comparator to check for [Crumb](Crumb.md)s equality.

Even if you can write your own custom comparator,
the arianna plugin is delivered with 3 ready-to-use comparators:

<dl>
<blockquote><dt><b>Name Comparator</b></dt>
<dt><code>org.softwareforge.struts2.breadcrumb.NameComparator</code></dt>
<dd>
A comparator that surprisingly just check crumb's name for equality.<br>
</dd></blockquote>

<blockquote><dt><b>Action Comparator</b></dt>
<dt><code>org.softwareforge.struts2.breadcrumb.ActionComparator</code></dt>
<dd>
This comparator checks the <i>action identity</i>, From its viewpoint a crumb is equals to another if they have the same namespace, action name and method name only. It does't care about crumb name.<br>
</dd></blockquote>

<blockquote><dt><b>Request Comparator</b></dt>
<dt><code>org.softwareforge.struts2.breadcrumb.RequestComparator</code></dt>
<dd>
This comparator besides to check for <i>action identity</i> (like the Action Comparator does) will also check request parameter equalities.<br>
</dd></blockquote>

<h3>User Defined Comparators</h3>

If none of the built-in comparator meets your needs, you can also write your own custom comparator. Just implement the <code>java.util.Comparator&lt;Crumb&gt;</code> interface and write the desired logic.<br>
<br>
For example<br>
<pre><code>public class MyComparator implements Comparator&lt;Crumb&gt;<br>
{<br>
    public int compare(Crumb c1, Crumb c2) {<br>
      // my custom comparing logic<br>
    }<br>
}<br>
</code></pre>
<blockquote>
<b>Note</b>
Any crumb comparator implementation must provide a public no-args constructor.<br>
</blockquote>