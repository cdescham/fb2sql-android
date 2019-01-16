$ git submodule add my_sub_project_git_url my-sub-project
Then include the project in your settings.gradle file which should look like this
include ':my-app', ':my-sub-project'
Finally, compile the project as a dependency in your application build.gradle file like this
dependencies {
  compile project(':my-sub-project')
}
