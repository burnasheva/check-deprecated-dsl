import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.MSBuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.ReSharperDuplicates
import jetbrains.buildServer.configs.kotlin.buildSteps.VisualStudioStep
import jetbrains.buildServer.configs.kotlin.buildSteps.msBuild
import jetbrains.buildServer.configs.kotlin.buildSteps.mstest
import jetbrains.buildServer.configs.kotlin.buildSteps.reSharperDuplicates
import jetbrains.buildServer.configs.kotlin.buildSteps.visualStudio
import jetbrains.buildServer.configs.kotlin.buildSteps.vstest
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.05"

project {

    vcsRoot(HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain)

    buildType(DuplicatesFinderAllFieldsAreEntered)
    buildType(DuplicatesFinderOnlyNecessaryFieldsAreEntered)
    buildType(VisualStudionSlnOnlyRequiredFields)
    buildType(MSBuildRequiredOnlyFields)
    buildType(MSBuildAllFields)
    buildType(VisualStudio2003onlyRequired)
    buildType(VisualStudionSlnAllFields)
    buildType(VisualStudioTestsOnlyRequired)
    buildType(VisualStudioTestsAllFields)
}

object DuplicatesFinderAllFieldsAreEntered : BuildType({
    name = "Duplicates Finder (all fields are entered)"

    vcs {
        root(HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain)
    }

    steps {
        reSharperDuplicates {
            name = "only required fields"
            excludeFiles = "**/*.txt"
            cltPath = "%teamcity.tool.jetbrains.resharper-clt.bundled%"
            cltPlatform = ReSharperDuplicates.Platform.CROSS_PLATFORM
            discardNamespaces = true
            discardTypesName = true
            discardFieldsName = true
            discardLocalVariablesName = true
            discardLiterals = false
            discardCost = "90"
            excludeOpeningComment = "skip"
            excludeRegionMessageSubstring = "substitution"
            debugOutput = true
            customCmdArgs = "param1=param2"
            dockerImage = "ubuntu"
            dockerImagePlatform = ReSharperDuplicates.ImagePlatform.Linux
            dockerPull = true
            dockerRunParameters = "--rm"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})

object DuplicatesFinderOnlyNecessaryFieldsAreEntered : BuildType({
    name = "Duplicates Finder (only necessary fields are entered)"

    vcs {
        root(HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain)
    }

    steps {
        reSharperDuplicates {
            name = "only required fields"
            cltPath = "%teamcity.tool.jetbrains.resharper-clt.DEFAULT%"
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})

object MSBuildAllFields : BuildType({
    name = "MSBuild, all fields"

    vcs {
        root(HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain)
    }

    steps {
        msBuild {
            name = "all fields"
            path = "PrimeService.Tests/PrimeService.Tests.csproj"
            version = MSBuildStep.MSBuildVersion.V15_0
            toolsVersion = MSBuildStep.MSBuildToolsVersion.V15_0
            platform = MSBuildStep.Platform.x64
            targets = "Clean Build"
            args = "--param"
            reduceTestFeedback = true
        }
    }
})

object MSBuildRequiredOnlyFields : BuildType({
    name = "MSBuild, required only fields"

    vcs {
        root(HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain)
    }

    steps {
        msBuild {
            name = "required only fields"
            path = "PrimeService.Tests/PrimeService.Tests.csproj"
            version = MSBuildStep.MSBuildVersion.V15_0
            toolsVersion = MSBuildStep.MSBuildToolsVersion.V15_0
            platform = MSBuildStep.Platform.x64
        }
    }
})

object VisualStudio2003onlyRequired : BuildType({
    name = "Visual Studio 2003, only required"

    vcs {
        root(HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain)
    }
})

object VisualStudioTestsAllFields : BuildType({
    name = "Visual Studio Tests, all fields"

    params {
        param("teamcity.dotnet.mstest.15.0", "%env.TEAMCITY_BUILDCONF_NAME%")
    }

    vcs {
        root(HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain)
    }

    steps {
        mstest {
            name = "all fields"
            mstestPath = "%teamcity.dotnet.mstest.15.0%"
            runSettings = "tests.cnf"
            metadata = "some"
            mstest_testlist = "Some.Tests"
            testsToRun = "Some.Tests.EXE"
            runIfUniqueMatchFound = true
            resultsFile = "result.here.txt"
            args = "--something else"
            coverage = dotcover {
            }
        }
    }
})

object VisualStudioTestsOnlyRequired : BuildType({
    name = "Visual Studio Tests, only required"

    params {
        param("teamcity.dotnet.vstest.12.0", "%env.TEAMCITY_BUILDCONF_NAME%")
    }

    vcs {
        root(HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain)
    }

    steps {
        vstest {
            name = "only required"
            includeTestFileNames = "Some.Tests.dll"
        }
    }
})

object VisualStudionSlnAllFields : BuildType({
    name = "Visual Studion (sln), all fields"

    vcs {
        root(HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain)
    }

    steps {
        visualStudio {
            name = "Visual Studio, all fields"
            path = "XUnitCore.sln"
            version = VisualStudioStep.VisualStudioVersion.vs2017
            runPlatform = VisualStudioStep.Platform.x86
            msBuildVersion = VisualStudioStep.MSBuildVersion.V15_0
            msBuildToolsVersion = VisualStudioStep.MSBuildToolsVersion.V15_0
            targets = "Clean Rebuild"
            configuration = "Debug"
            platform = "x86"
            args = "--param"
        }
    }
})

object VisualStudionSlnOnlyRequiredFields : BuildType({
    name = "Visual Studion (sln), only required fields"

    vcs {
        root(HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain)
    }

    steps {
        visualStudio {
            name = "Visual Studio, required only"
            path = "XUnitCore.sln"
            version = VisualStudioStep.VisualStudioVersion.vs2019
            runPlatform = VisualStudioStep.Platform.x86
            msBuildVersion = VisualStudioStep.MSBuildVersion.V16_0
            msBuildToolsVersion = VisualStudioStep.MSBuildToolsVersion.V16_0
        }
    }
})

object HttpsGithubComBurnashevaXunitDotnet6gitRefsHeadsMain : GitVcsRoot({
    name = "https://github.com/burnasheva/xunit_dotnet6.git#refs/heads/main"
    url = "https://github.com/burnasheva/xunit_dotnet6.git"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
})
