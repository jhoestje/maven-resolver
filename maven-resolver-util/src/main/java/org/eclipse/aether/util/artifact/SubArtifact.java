package org.eclipse.aether.util.artifact;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.Objects;

import org.eclipse.aether.artifact.AbstractArtifact;
import org.eclipse.aether.artifact.Artifact;

/**
 * An artifact whose identity is derived from another artifact. <em>Note:</em> Instances of this class are immutable and
 * the exposed mutators return new objects rather than changing the current instance.
 */
public final class SubArtifact<T>
    extends AbstractArtifact<T>
{

    private final Artifact<T> mainArtifact;

    private final String classifier;

    private final String extension;

    private final T file;

    private final Map<String, String> properties;

    /**
     * Creates a new sub artifact. The classifier and extension specified for this artifact may use the asterisk
     * character "*" to refer to the corresponding property of the main artifact. For instance, the classifier
     * "*-sources" can be used to refer to the source attachment of an artifact. Likewise, the extension "*.asc" can be
     * used to refer to the GPG signature of an artifact.
     * 
     * @param mainArtifact The artifact from which to derive the identity, must not be {@code null}.
     * @param classifier The classifier for this artifact, may be {@code null} if none.
     * @param extension The extension for this artifact, may be {@code null} if none.
     */
    public SubArtifact( Artifact<T> mainArtifact, String classifier, String extension )
    {
        this( mainArtifact, classifier, extension, (T) null );
    }

    /**
     * Creates a new sub artifact. The classifier and extension specified for this artifact may use the asterisk
     * character "*" to refer to the corresponding property of the main artifact. For instance, the classifier
     * "*-sources" can be used to refer to the source attachment of an artifact. Likewise, the extension "*.asc" can be
     * used to refer to the GPG signature of an artifact.
     * 
     * @param mainArtifact The artifact from which to derive the identity, must not be {@code null}.
     * @param classifier The classifier for this artifact, may be {@code null} if none.
     * @param extension The extension for this artifact, may be {@code null} if none.
     * @param file The file for this artifact, may be {@code null} if unresolved.
     */
    public SubArtifact( Artifact<T> mainArtifact, String classifier, String extension, T file )
    {
        this( mainArtifact, classifier, extension, null, file );
    }

    /**
     * Creates a new sub artifact. The classifier and extension specified for this artifact may use the asterisk
     * character "*" to refer to the corresponding property of the main artifact. For instance, the classifier
     * "*-sources" can be used to refer to the source attachment of an artifact. Likewise, the extension "*.asc" can be
     * used to refer to the GPG signature of an artifact.
     * 
     * @param mainArtifact The artifact from which to derive the identity, must not be {@code null}.
     * @param classifier The classifier for this artifact, may be {@code null} if none.
     * @param extension The extension for this artifact, may be {@code null} if none.
     * @param properties The properties of the artifact, may be {@code null}.
     */
    public SubArtifact( Artifact<T> mainArtifact, String classifier, String extension, Map<String, String> properties )
    {
        this( mainArtifact, classifier, extension, properties, null );
    }

    /**
     * Creates a new sub artifact. The classifier and extension specified for this artifact may use the asterisk
     * character "*" to refer to the corresponding property of the main artifact. For instance, the classifier
     * "*-sources" can be used to refer to the source attachment of an artifact. Likewise, the extension "*.asc" can be
     * used to refer to the GPG signature of an artifact.
     * 
     * @param mainArtifact The artifact from which to derive the identity, must not be {@code null}.
     * @param classifier The classifier for this artifact, may be {@code null} if none.
     * @param extension The extension for this artifact, may be {@code null} if none.
     * @param properties The properties of the artifact, may be {@code null}.
     * @param file The file for this artifact, may be {@code null} if unresolved.
     */
    public SubArtifact( Artifact<T> mainArtifact, String classifier, String extension, Map<String, String> properties,
                        T file )
    {
        this.mainArtifact = requireNonNull( mainArtifact, "main artifact cannot be null" );
        this.classifier = classifier;
        this.extension = extension;
        this.file = file;
        this.properties = copyProperties( properties );
    }

//    private SubArtifact( Artifact<T> mainArtifact, String classifier, String extension, T file,
//                         Map<String, String> properties )
//    {
//        // NOTE: This constructor assumes immutability of the provided properties, for internal use only
//        this.mainArtifact = mainArtifact;
//        this.classifier = classifier;
//        this.extension = extension;
//        this.file = file;
//        this.properties = properties;
//    }

    public String getGroupId()
    {
        return mainArtifact.getGroupId();
    }

    public String getArtifactId()
    {
        return mainArtifact.getArtifactId();
    }

    public String getVersion()
    {
        return mainArtifact.getVersion();
    }

    public String getBaseVersion()
    {
        return mainArtifact.getBaseVersion();
    }

    public boolean isSnapshot()
    {
        return mainArtifact.isSnapshot();
    }

    public String getClassifier()
    {
        return expand( classifier, mainArtifact.getClassifier() );
    }

    public String getExtension()
    {
        return expand( extension, mainArtifact.getExtension() );
    }

    public T getStorage()
    {
        return file;
    }

    public Artifact<T> setStorage( T file )
    {
        if ( Objects.equals( this.file, file ) )
        {
            return this;
        }
        return new SubArtifact<T>( mainArtifact, classifier, extension, properties, file );
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public Artifact<T> setProperties( Map<String, String> properties )
    {
        if ( this.properties.equals( properties ) || ( properties == null && this.properties.isEmpty() ) )
        {
            return this;
        }
        return new SubArtifact<T>( mainArtifact, classifier, extension, properties, file );
    }

    private static String expand( String pattern, String replacement )
    {
        String result = "";
        if ( pattern != null )
        {
            result = pattern.replace( "*", replacement );

            if ( replacement.length() <= 0 )
            {
                if ( pattern.startsWith( "*" ) )
                {
                    int i = 0;
                    for ( ; i < result.length(); i++ )
                    {
                        char c = result.charAt( i );
                        if ( c != '-' && c != '.' )
                        {
                            break;
                        }
                    }
                    result = result.substring( i );
                }
                if ( pattern.endsWith( "*" ) )
                {
                    int i = result.length() - 1;
                    for ( ; i >= 0; i-- )
                    {
                        char c = result.charAt( i );
                        if ( c != '-' && c != '.' )
                        {
                            break;
                        }
                    }
                    result = result.substring( 0, i + 1 );
                }
            }
        }
        return result;
    }

	@Override
	protected Artifact<T> newInstance(String version, Map<String, String> properties, T file) {
		return new SubArtifact<T>( mainArtifact, classifier, extension, properties, file );
	}

}
