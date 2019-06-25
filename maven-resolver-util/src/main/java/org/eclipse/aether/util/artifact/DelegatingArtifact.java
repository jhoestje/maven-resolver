package org.eclipse.aether.util.artifact;

import static java.util.Objects.requireNonNull;

import java.util.Map;

import org.eclipse.aether.artifact.AbstractArtifact;
import org.eclipse.aether.artifact.Artifact;

/**
 * An artifact that delegates to another artifact instance. This class serves as a base for subclasses that want to
 * carry additional data fields.
 */
public abstract class DelegatingArtifact<T>
    extends AbstractArtifact<T>
{

    private final Artifact<T> delegate;

    /**
     * Creates a new artifact instance that delegates to the specified artifact.
     *
     * @param delegate The artifact to delegate to, must not be {@code null}.
     */
    protected DelegatingArtifact( Artifact<T> delegate )
    {
        this.delegate = requireNonNull( delegate, "delegate artifact cannot be null" );
    }

    /**
     * Creates a new artifact instance that delegates to the specified artifact. Subclasses should use this hook to
     * instantiate themselves, taking along any data from the current instance that was added.
     *
     * @param delegate The artifact to delegate to, must not be {@code null}.
     * @return The new delegating artifact, never {@code null}.
     */
    protected abstract DelegatingArtifact<T> newInstance( Artifact<T> delegate );

    public String getGroupId()
    {
        return delegate.getGroupId();
    }

    public String getArtifactId()
    {
        return delegate.getArtifactId();
    }

    public String getVersion()
    {
        return delegate.getVersion();
    }

    public Artifact<T> setVersion( String version )
    {
        Artifact<T> artifact = delegate.setVersion( version );
        if ( artifact != delegate )
        {
            return newInstance( artifact );
        }
        return this;
    }

    public String getBaseVersion()
    {
        return delegate.getBaseVersion();
    }

    public boolean isSnapshot()
    {
        return delegate.isSnapshot();
    }

    public String getClassifier()
    {
        return delegate.getClassifier();
    }

    public String getExtension()
    {
        return delegate.getExtension();
    }

    public T getStorage()
    {
        return delegate.getStorage();
    }

    public Artifact<T> setStorage( T file )
    {
        Artifact<T> artifact = delegate.setStorage( file );
        if ( artifact != delegate )
        {
            return newInstance( artifact );
        }
        return this;
    }

    public String getProperty( String key, String defaultValue )
    {
        return delegate.getProperty( key, defaultValue );
    }

    public Map<String, String> getProperties()
    {
        return delegate.getProperties();
    }

    public Artifact<T> setProperties( Map<String, String> properties )
    {
        Artifact<T> artifact = delegate.setProperties( properties );
        if ( artifact != delegate )
        {
            return newInstance( artifact );
        }
        return this;
    }

    @SuppressWarnings("unchecked")
	@Override
    public boolean equals( Object obj )
    {
        if ( obj == this )
        {
            return true;
        }

        if ( obj instanceof DelegatingArtifact )
        {
            return delegate.equals( ( (DelegatingArtifact<T>) obj ).delegate );
        }

        return delegate.equals( obj );
    }

    @Override
    public int hashCode()
    {
        return delegate.hashCode();
    }

    @Override
    public String toString()
    {
        return delegate.toString();
    }

}
