# ProjectKorra Addon Backbone

Hello, are you tired to manage configs in AddonAbility#load() method too?
Are you tired to register and unregister abilities' listeners in code?
Then you might be interested in this addon packs framework I made for myself and share with you. Try it!

# Files you have to change for your own project
These files are: **pom.xml**, **plugin.yml**, **PK-AddonBackbone.iml**
### Project structure 
![Project structure in IntelliJ IDEA](https://sun9-80.userapi.com/impg/f10AebRmHFUOgB81L2xlMPbY8fB_6my4avx4_g/CEDInn7Fdw8.jpg?size=354x448&quality=96&sign=255ef89867aa795b3318983b1e2bd5a9&type=album)
### pom.xml

This file's header contains some variables you have to change for your own project.
Name, description and etc. These values will be used in **plugin.yml**.

![pom.xml variables to change](https://sun9-54.userapi.com/impg/28cjnIjKITY_cfFnXHkvdhQpho9cCmBFCWP2wg/EJWGDD2u44g.jpg?size=1161x333&quality=96&sign=2de0628f3019da80ac8a1759751da70f&type=album)
- **groupId**
The groupId is *a parameter indicating the group or individual that created a project*, which is often a reversed company domain name.
>In ProjectKorra community the most often pattern is:
me.\<author\>.\<project name\>
In my case it's *me.dreig_michihi.addonbackbone*.
- **artifactId**
ArtifactId is *the name of the jar without version*. If you created it, then you can choose whatever name you want with lowercase letters and no strange symbols.
- **name**, **version**
Name and version of the project, will be used in plugin.yml and generated file's name.
- **description**
Description of your plugin, will be used in plugin.yml too.

### plugin.yml
The file used by Spigot to get some info about the plugin.
Most of info here gotten from pom.xml file (2nd section).

![plugin.yml](https://sun9-25.userapi.com/impg/L90pEWyK02wqBddE17JjPwQyHikDLqAJGcWU8g/bTFdYOy5j_M.jpg?size=915x446&quality=96&sign=c57a71ca4b55fec2f34756a286ad3f2a&type=album)

- **main** - Full path to main plugin's class extending JavaPlugin abstract class.
- **abilities-package-base** - Full path to the package, where abilities and listeners are placed.
- **debug-info** - If you enable this field, you'll get detailed console output how the plugin loads abilities, listeners and config.
- **depend** - Current project depends ProjectKorra, because it uses the PK API.
- **author** - you are :-)
- **authors** - *WE* are ;-)

### PK-AddonBackbone.iml

Just rename this file.
>Actually I don't even know well what is it's purpose **XD**
# Making an ability
### PluginAbility interface
To make an ability, you have to ***implement* PluginAbility** interface *instead of AddonAbility*.
>PluginAbility interface has unified implementations of methods *#load, #stop, #getAuthor, #getVersion* so every ability in /b h will show the project author's name and the same version.
### Adding Instructions and Description
To add Instructions and Description, you have to add relevant annotations **@Description** and **@Instructions** above the abilty class's declaration.

![Description and Instructions annotations](https://sun9-51.userapi.com/impg/a_s0_2f7Nz-aOHEh6FtKfx7UFuQhStg104EEKQ/zAfssdNNy0k.jpg?size=710x192&quality=96&sign=7c9e5b456726b55927ee6d6099d48c1e&type=album)
### Adding configurable variables
To make an ability's field configurable, add static variable with annotation **@Configurable** in it's declaration.
To add a Comment to config, add annotation **@Comment**.
Don't forget about ProjectKorra's **@Attribute** annotation, so the variable could be taken or modified by other developers in their plugins.

![creating configurable variables](https://sun9-37.userapi.com/impg/CN3mj5t_2i2SFUagMHmLuUuGW6NkP7GuKUlISA/rBXVS6AXfS4.jpg?size=735x665&quality=96&sign=65694b6ae96aa7ac06574d74d0210c57&type=album)

**That's how generated config will look like:**
![enter image description here](https://sun9-55.userapi.com/impg/KseSr_tZvvaWlQgIIvQf_au5oKKvGru5CfE3sg/QoMFEMnnvAk.jpg?size=674x300&quality=96&sign=522fbf9fd6dabbe79f646afa6e898ed2&type=album)

**Description and Instructions won't be hardcoded, they'll be configurable too!**
![enter image description here](https://sun9-24.userapi.com/impg/FizDbZDy2umcCh_C00UbU1IsG7p2BLlEU1o8Kg/1UFf0zaHqwM.jpg?size=518x267&quality=96&sign=ed927d82d081e39a2657ac26b12ef64c&type=album)

# Adding Listeners

Something interesting here...
To make a Listener, all you have to do - make a separate Listener file somewhere in package for abilities you chose before.
**All the Listeners there will be registered and unregistered automatically!**
>In my case - it's me.dreig_michihi.addonbackbone.bending

Also you can use annotation @AssociatedAbility("<AbilityClass's name in the same package here>"), so the listener will be enabled only when the associated ability is enabled!

![Listener code](https://sun9-10.userapi.com/impg/qSNIUt_ExvYwphZGz_He9Pr7deLEZiUDgZNZsw/gX6sauyXP9s.jpg?size=1271x646&quality=96&sign=dbc68069f7f87e3d46802716978a62a1&type=album)
So you can have separate Listeners for each ability and it won't look too bad for your eyes.

# Overriding CoreAbility#isEnabled method

I couldn't add default overriding implementation for this method in PluginAbility interface, because CoreAbility is overriding it too.

The easiest way to configurable it:
![Overriding isEnabled code](https://sun9-52.userapi.com/impg/GteE09Oc1teknTcP6aPl9GI3-XHGJWX7DyD_Lw/dBvEgKB4LOA.jpg?size=539x91&quality=96&sign=dbda0994c9b979f95564913b64befb45&type=album)

[I want YOU to support me on Boosty if you liked this or any of my other works ;)](https://boosty.to/dreig_michihi)
![[I want YOU to support me on Boosty if you liked this or any of my other works ;)](https://boosty.to/dreig_michihi)](https://i.etsystatic.com/28001671/r/il/0f66d4/2963569023/il_fullxfull.2963569023_rmjx.jpg)