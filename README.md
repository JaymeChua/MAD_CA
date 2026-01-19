# MAD_CA
This is a development done to satisfy the assignment portion for the module “Mobile App development” under Ngee Ann Polytechnic for AY24/25

## LLM Usage
### Tools used
Gemini 2.0 Flash

### Example Prompts Used
#### How can I rework the UI for the mole game to be more intuitive, as it is hard to tell the change from "Hole" to "Mole"?
Before AI Influence: The initial grid used simple Text("Mole") and Text("Hole") inside buttons. It was functional but lacked game-like intuition.
After AI Influence: The LLM suggested using Icons.Default.Face and conditional ButtonDefaults.buttonColors.

Why: To reduce cognitive load. A brown button with a face icon is instantly recognizable as a target compared to reading text in a fast-paced game.
Learnt: Visual cues are more important and functional than text

#### Technical Case Study: Resolving Gradle Build Conflicts
The Issue: KSP (Kotlin Symbol Processing) & KAPT (Kotlin Annotation Processing Tool) Kotlin Version Mismatch
During the transition to the Advanced requirements, I initially attempted to use KSP for the Room Database implementation as I received a warning "This library supports using KSP instead of KAPT, which greatly improves performance. Learn more: https://developer.android.com/studio/build/migrate-to-ksp".
However, this triggered a critical build failure: Unable to find method 'getJvmDefault()' in KotlinJvmCompilerOptions.

The Cause: The getJvmDefault() error is a classic "binary incompatibility" issue. It occurs when the version of the KSP plugin is not perfectly aligned with the version of the Kotlin Gradle Plugin (KGP). Even a minor version mismatch (e.g., trying to use a KSP version built for Kotlin 2.1.0 while the project is on 2.0.21) causes the Gradle daemon to crash during the metadata transformation phase. This further led to IOExceptions and FileNotFoundExceptions as the Gradle cache became corrupted during the failed syncs.

##### Trying Manual Solutions
1. Go to Build > Clean Project. Then go to File > Invalidate Caches... and select "Clear file system cache and Local History" and "Invalidate and Restart."
2. Go to edit configurations under app and add gradle app:uninstallAll and run the app again
3. Delete the Specific Cache Folder: Manually delete the corrupted cache directory. Close your IDE > Navigate to specific cache folder (8.13 for me) > Deleted the 8.13 folder entirely
Re-open your project and sync/build.
None of these solutions worked

##### AI-Assisted Solution
Switch back from KSP to KAPT: While KSP is the modern successor, KAPT has a longer history of stability across varied Gradle versions.
Action: The AI provided a refactor of the build.gradle.kts file to remove the KSP plugin and re-integrate id("kotlin-kapt").
Refactor Detail: * Before: ksp("androidx.room:room-compiler:$version")
After: kapt("androidx.room:room-compiler:$version")
Learnt: While KSP offers better performance, KAPT's maturity and its compatibility with my existing environment saved dozens of hours of "dependency hell." Modern Android toolchains (Compose, Room, and Kotlin) are deeply interdependent. Moving forward, I will prioritize established configurations (like KAPT) for core project infrastructure to ensure that my development time is spent on feature logic rather than environment troubleshooting.

#### I have a messy paragraph about my Gradle issues with KSP and KAPT. It's hard to read and has grammatical mistakes. Can you rewrite it to sound professional for a technical report and summarize the key fix?
Before AI Influence: So I had this error because KSP was not working and it keep saying getjvmdefault method not found and my gradle was broke and I had to delete the cache folder 8.13 and a whole bunch of other manual fixes and then I decided to just use KAPT because it is what I know from class and it works better than KSP which was giving me a headache with the versions not matching up.
After AI Influence: Above
Learnt: AI is good at technical translation. It can take raw, "stream-of-consciousness" troubleshooting notes and transform them into structured, professional documentation.
