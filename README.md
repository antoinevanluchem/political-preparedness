# Code Review 

## Reviewer Note 
> Well-formatted and organized code along with smoothly working app with correctly implemented MVVM pattern, you did a great job here.
> 
> Many skills showed here.
> 
> MVVM pattern.
> Room for persisting data.
> Motion effects using MotionLayout.
> Kotlin Coroutines and coroutines scopes.
> DataBinding.
>
> it was a pleasure reviewing your project, thank you.
>
> Congratulations 🏆
>

## Android UI/UX 
✅ Build a navigable interface consisting of multiple screens of functionality and data 
> App includes 3 main screens, launch, listing, and representatives screens 👌
> Navigation component utilized correctly for fragment-based navigation
>

✅ Construct interfaces that adhere to Android standards and display appropriately on screens of different size and resolution 
> Great,ConstraintLayout allows you to create large and complex layouts with a flat view hierarchy (no nested view groups). It's similar to RelativeLayout in that all views are laid out according to relationships between sibling views and the parent layout, but it's more flexible than RelativeLayout and easier to use with Android Studio's Layout Editor. here are a couple of resources to master ConstrainLayout:
> 
> [ConstraintLayout Tutorial for Android: Complex Layouts](https://www.kodeco.com/9475-constraintlayout-tutorial-for-android-complex-layouts)
> 
> [5 tips to master ConstraintLayout](https://www.youtube.com/watch?v=hqEfshM5Vfw)
> 
> Text resources, dimensions, and colors are stored in appropriate res files 👏 ,RecyclerView implemented efficiently with the new improved ListAdapter


✅ Animate UI components to better utilize screen real estate and create engaging content 
> The collapsing representative form views motion behavior works as expected, well-done 👌
> 
> As stated in the [documentation](https://developer.android.com/develop/ui/views/animations/motionlayout):
> 
> MotionLayout is a layout type that helps you manage motion and widget animation in your app. It is a subclass of ConstraintLayout and builds upon its rich layout capabilities.
> 
> So, it’s really powerful, it has a lot of potentials, but it only works with its direct children. It does not support nested layout hierarchies or activity transitions and this should be taken into consideration. a couple of extra resources:
> 
> [Complex UI/Animations on Android — featuring MotionLayout](https://proandroiddev.com/complex-ui-animations-on-android-featuring-motionlayout-aa82d83b8660)
> [MotionLayout + MotionScene - Motion Tags Ep1](https://www.youtube.com/watch?v=o8c1RO3WgBA&ab_channel=AndroidDevelopers)
> [Creating Android animations with MotionLayout and MotionEditor](https://bignerdranch.com/blog/creating-android-animations-with-motionlayout-and-motioneditor/)

## Local and Network data 
✅ Connect to and consume data from a remote data source such as a RESTful API 
> elections data retrieved successfully with the help of Retrofit and moshi, well-done 👏
> network calls are executed inside a coroutine scope as loading network requests is prohibited on UI thread.


✅ Load network resources, such as Bitmap Images, dynamically and on-demand
> Great choice, Glide(opens in a new tab) is a fast and efficient image loading library for Android, [Picasso](https://square.github.io/picasso/) is a great choice as well
✅ Store data locally on the device for use between application sessions and/or offline use
> Room very well implemented for elections saving with appropriate TypeConverters defined to entities.

## Android system and hardware integration 
✅ Architect application functionality using MVVM
> Really good, MVVM is implemented very well here, with Repository pattern implemented for elections to separate responsibilities as clean architecture suggests and as recommended by G[oogle App architecture guide
](https://developer.android.com/topic/architecture)

✅ Implement logic to handle and respond to hardware and system events that impact the Android Lifecycle
> As ViewModel handles business logic, View should handle system interactions, Permissions, Intents, Locations , Notifications..etc. well-done 👏


✅ Utilize system hardware to provide the user with advanced functionality and features
> Accessing Location feature well implemented. 👏


