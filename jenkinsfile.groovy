import hudson.tasks.test.AbstractTestResultAction
import hudson.tasks.junit.CaseResult

def speedUp = '--configure-on-demand --daemon --parallel'
def nebulaReleaseScope = (env.GIT_BRANCH == 'origin/master') ? '' : "-Prelease.scope=patch"
def nebulaRelease = "-x prepare -x release snapshot ${nebulaReleaseScope}"
def gradleDefaultSwitches = "${speedUp} ${nebulaRelease}"
def gradleAdditionalTestTargets = "integrationTest"
def gradleAdditionalSwitches = "shadowJar"

def getTestSummary = { ->
    def testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)
	print(testResultAction)
    def summary = ""

    if (testResultAction != null) {
      def  total = testResultAction.getTotalCount()
      def  failed = testResultAction.getFailCount()
      def  skipped = testResultAction.getSkipCount()
	   
	
	    println("Total No. Of Tests:::"+total)
        summary = "Passed: " + (total - failed - skipped)
        summary = summary + (", Failed: " + failed)
        summary = summary + (", Skipped: " + skipped)
    } else {
        summary = "No tests found"
    }
   println(summary)
}


node {
	stage('Payment Testsuite Execution'){
		checkout([$class: 'GitSCM',
		branches: [[name: '*/master']],
		userRemoteConfigs: [
		[url: 'https://github.com/SujataKale97/test.git',
		credentialsId: 'ea4c3770-b2ed-4639-9ffc-cc3e586e454c']
		]])  
			
                 bat ' gradlew ' +
                      
                 "clean ${gradleDefaultSwitches} clean build ${gradleAdditionalTestTargets} ${gradleAdditionalSwitches} test -Ppayment --refresh-dependencies "
	
		publishHTML([reportDir: 'test-output', reportFiles: 'PaymentServiceReport.html', reportName: 'Payment Test-suite Report'])
		
        	/*bat ' echo >> test-output/testng-failed.xml'
		bat ' echo >> test-output/testng-failed.xml'
		step $class: 'JUnitResultArchiver', testResults: 'test-output/testng-*.xml'*/
		getTestSummary()
		
	
	}
	/*stage('Email')
        {
            env.ForEmailPlugin = env.WORKSPACE      
            emailext ( attachmentsPattern: "test-output/PaymentServiceReport.html", 
            body: " ${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}", 
            subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
	    to: "kale.babanrao@happiestminds.com"
            
	     )
		
        }*/
/*	stage('SonarQube analysis') {
   // ws('C:\\Apps\\Jenkins\\jobs\\trial') {
    // requires SonarQube Scanner 2.8+
    def scannerHome = tool 'sonarScanner';
    withSonarQubeEnv('sonar') {
	   bat "gradlew -Dsonar.analysis.mode "
      bat "${scannerHome}/StartSonar.bat"
    }
  
}*/
}
