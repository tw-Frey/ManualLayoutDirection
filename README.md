有時候，`canvas` 畫不出 `TextView` 的 `compoundDrawablesRelative`  
原因可能出在 `layoutDirection` ...  

例如
```kotlin
    val item = layoutInflater.inflate(R.layout.item_tv, null)
    item.measure(widthSpec, heightSpec)
    item.layout(.., .., .., ..)

    val bitmap = Bitmap.createBitmap(.., .., Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.translate(item.left.toFloat(), item.top.toFloat())
    item.draw(canvas)
```
item 是有 size 的  
`canvas` 也畫出文字和背景  
觀察 `compoundDrawables` (drawableTop/drawableBottom) 是有出現的  
但 `compoundDrawablesRelative` (drawableStart/drawableEnd) 沒有畫出來  
去翻 `TextView` 的 `onDraw()` 原始碼  

擷取如下  
```java
    final Drawables dr = mDrawables;
    if (dr != null) {
        ...
        if (dr.mShowing[Drawables.LEFT] != null) {
            ...
            dr.mShowing[Drawables.LEFT].draw(canvas);
            ...
        }
        ...
        if (dr.mShowing[Drawables.RIGHT] != null) {
            ...
            dr.mShowing[Drawables.RIGHT].draw(canvas);
            ...
        }
        ...
        if (dr.mShowing[Drawables.TOP] != null) {
            ...
            dr.mShowing[Drawables.TOP].draw(canvas);
            ...
        }
        ...
        if (dr.mShowing[Drawables.BOTTOM] != null) {
            ...
            dr.mShowing[Drawables.BOTTOM].draw(canvas);
            ...
        }
    }
```
上述的 `dr` 只有儲存四個方向：`TOP`、`BOTTOM`、`LEFT`、`RIGHT`  
那後面兩個又如何對應到 `drawableStart` 和 `drawableEnd`  
在 `TextView` 原始碼裡  
關鍵是在 `onResolveDrawables()`，它是在 `resolveDrawables()` 被呼叫  
不過 `onResolveDrawables()` 或 `resolveDrawables()` 無法直接呼叫  
只能間接被使用  
例如  
在 `setLayoutDirection()` 裡有 `resolveRtlPropertiesIfNeeded()`  
它裡面就會使用到 `resolveDrawables()`  
應用 `setLayoutDirection()` 就能讓 `canvas` 畫出 `TextView` 的 `compoundDrawablesRelative`  

如下

![截圖](/doc/image.png)

可以這麼解讀  
因為 view 吹出來後  
沒有加入 parent  
就不知道 `layoutDirection`  
以致 `compoundDrawablesRelative` 無法注入 `compoundDrawables`  
因此 `compoundDrawables` 的 `drawableLeft` 和 `drawableRight` 就為空  
所以實際上是 `canvas` 畫不出 `drawableLeft` 和 `drawableRight` (因為是空的)  

---

#### 從上面的解讀可以找出四個解法  

### 1. 糾正小孩子：不可以用新東西
```xml
<TextView
    ...
    app:drawableEndCompat="@drawable/icon_chevron_grlight_20"
    ...
```
改成
```xml
<TextView
    ...
    app:drawableRightCompat="@drawable/icon_chevron_grlight_20"
    ...
```
_直接將 drawableStart/drawableEnd 換成 drawableLeft/drawableRight_

---

### 2. 譴責父母：會生也要會養
```xml
<RelativeLayout
    android:layoutDirection="locale"
    ...
    <TextView
    ...
```
或者
```xml
<RelativeLayout
    ...
    <TextView
        android:layoutDirection="locale"
    ...
```

_直接在 parent 或 child 設定 `android:layoutDirection="locale"`_  
_(either **parent** or **chlid**, 不要兩個都設置)_

---

### 3. 督促肇事者：把事情搞出來，要擦屁股
```kotlin
    val item2 = layoutInflater.inflate(R.layout.item_tv, null)
    item2.layoutDirection = LAYOUT_DIRECTION_LOCALE
    item2.measure(widthSpec, heightSpec)
    item2.layout(item0.left, item0.top, item0.right, item0.bottom)
```
_把 view 吹出來又不給加，請記得設定 setLayoutDirection(LAYOUT_DIRECTION_LOCALE)_

---

### 4. 大環境 淺規則：來到 LTR 國家，就不要期待有 RTL 友善
```xml
    <application
        ...
        android:supportsRtl="false"
        ...
        tools:replace="android:supportsRtl">
        ...
```
_直接在 AndroidManifest.xml 將 android:supportsRtl 設為 false_
