package me.philcali.config.ssm;

import java.util.Iterator;
import java.util.Objects;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathResult;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;

public class ParameterIterator implements Iterator<Parameter> {
    private static final int PER_PAGE = 100;
    private final AWSSimpleSystemsManagement ssm;
    private final GetParametersByPathRequest request;
    private Iterator<Parameter> currentPage;

    public ParameterIterator(final String groupName, final AWSSimpleSystemsManagement ssm) {
        this.ssm = ssm;
        this.request = new GetParametersByPathRequest()
                .withPath(groupName + "/")
                .withRecursive(true)
                .withWithDecryption(true)
                .withMaxResults(PER_PAGE);
    }

    private void fillPage() {
        final GetParametersByPathResult result = ssm.getParametersByPath(request);
        result.setNextToken(result.getNextToken());
        currentPage = result.getParameters().iterator();
    }

    @Override
    public boolean hasNext() {
        if (Objects.isNull(currentPage) || (!currentPage.hasNext() && Objects.isNull(request.getNextToken()))) {
            fillPage();
        }
        return currentPage.hasNext();
    }

    @Override
    public Parameter next() {
        return currentPage.next();
    }

}
