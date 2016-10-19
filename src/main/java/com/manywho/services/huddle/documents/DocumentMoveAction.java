package com.manywho.services.huddle.documents;

import com.manywho.sdk.entities.describe.DescribeValue;
import com.manywho.sdk.entities.describe.DescribeValueCollection;
import com.manywho.sdk.enums.ContentType;
import com.manywho.sdk.services.describe.actions.AbstractAction;

public class DocumentMoveAction extends AbstractAction {
    @Override
    public String getUriPart() {
        return "documents/move";
    }

    @Override
    public String getDeveloperName() {
        return "Document: Move";
    }

    @Override
    public String getDeveloperSummary() {
        return "Move a document to another folder";
    }

    @Override
    public DescribeValueCollection getServiceInputs() {
        DescribeValueCollection describeValues = new DescribeValueCollection();
        describeValues.add(new DescribeValue("File", ContentType.Object, true, null, "$File"));
        describeValues.add(new DescribeValue("Folder ID", ContentType.String, true));

        return describeValues;
    }

    @Override
    public DescribeValueCollection getServiceOutputs() {
        return new DescribeValueCollection();
    }
}
