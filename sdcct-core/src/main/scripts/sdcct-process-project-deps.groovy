import org.apache.commons.lang3.StringUtils
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler

def localRepo = project.projectBuildingRequest.localRepository
def soapuiProjectArtifact = new DefaultArtifact("com.smartbear.soapui", "soapui-project", "5.2.1", Artifact.SCOPE_TEST, "pom", StringUtils.EMPTY,
    new DefaultArtifactHandler())
def soapuiProjectArtifactLocalRepoFile = new File(localRepo.basedir, "${localRepo.pathOf(soapuiProjectArtifact)}.${soapuiProjectArtifact.type}")
def soapuiProjectArtifactFileName = soapuiProjectArtifactLocalRepoFile.name
def soapuiProjectArtifactUrl = "https://github.com/SmartBear/soapui/raw/${soapuiProjectArtifact.version}/pom.xml"
def soapuiProjectArtifactDir = new File(((String) project.properties["project.build.directory"]), soapuiProjectArtifact.artifactId)
def soapuiProjectArtifactFile = new File(soapuiProjectArtifactDir, soapuiProjectArtifactFileName)

if (!soapuiProjectArtifactLocalRepoFile.exists()) {
    if (!soapuiProjectArtifactDir.exists()) {
        soapuiProjectArtifactDir.mkdirs()
    }
    
    ant.get(src: soapuiProjectArtifactUrl, dest: soapuiProjectArtifactFile)
    
    ant.exec(executable: "mvn", failonerror: true) {
        ant.arg(value: "-q")
        ant.arg(value: "install:install-file")
        ant.arg(value: "-DgroupId=${soapuiProjectArtifact.groupId}")
        ant.arg(value: "-DartifactId=${soapuiProjectArtifact.artifactId}")
        ant.arg(value: "-Dversion=${soapuiProjectArtifact.version}")
        ant.arg(value: "-Dpackaging=${soapuiProjectArtifact.type}")
        ant.arg(value: "-Dfile=${soapuiProjectArtifactFile}")
    }
}
