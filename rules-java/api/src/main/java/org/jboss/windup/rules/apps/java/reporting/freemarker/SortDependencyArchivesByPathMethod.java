package org.jboss.windup.rules.apps.java.reporting.freemarker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import freemarker.template.DefaultIterableAdapter;
import org.jboss.windup.reporting.freemarker.WindupFreeMarkerMethod;
import org.jboss.windup.rules.apps.java.dependencyreport.DependencyReportToArchiveEdgeModel;
import org.jboss.windup.rules.apps.java.model.comparator.DependencyReportToEdgeComparator;
import org.jboss.windup.util.ExecutionStatistics;

import freemarker.template.TemplateModelException;

/**
 * Takes a list of DependencyReportToArchiveEdgeModel and orders them according to their path.
 *
 * For example, DependencyReportToArchiveEdgeModel with this structure:
 *
 * <ul>
 * <li>/CProject</li>
 * <li>/BProject</li>
 * <li>/AProject</li>
 * </ul>
 *
 * Will be returned as:
 *
 * <ul>
 * <li>/AProject</li>
 * <li>/BProject</li>
 * <li>/CProject</li>
 * </ul>
 *
 */
public class SortDependencyArchivesByPathMethod implements WindupFreeMarkerMethod
{
    private static final String NAME = "sortDependencyArchivesByPathAscending";

    @Override
    public String getMethodName()
    {
        return NAME;
    }

    @Override
    public String getDescription()
    {
        return "Takes an Iterable<" + DependencyReportToArchiveEdgeModel.class.getSimpleName() + "> and returns them, ordered alphabetically.";
    }

    @Override
    public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException
    {
        ExecutionStatistics.get().begin(NAME);
        if (arguments.size() != 1)
        {
            throw new TemplateModelException("Error, method expects one argument (Iterable<DependencyReportToArchiveEdgeModel>)");
        }

        DefaultIterableAdapter argModel = (DefaultIterableAdapter) arguments.get(0);

        @SuppressWarnings("unchecked")
        //Iterable<DependencyReportToArchiveEdgeModel> edges = (Iterable<DependencyReportToArchiveEdgeModel>) stringModelArg.getWrappedObject();
        Iterable<DependencyReportToArchiveEdgeModel> edges = (Iterable<DependencyReportToArchiveEdgeModel>) argModel.getAdaptedObject(DependencyReportToArchiveEdgeModel.class);

        List<DependencyReportToArchiveEdgeModel> list = new ArrayList<>();
        for (DependencyReportToArchiveEdgeModel edge : edges)
        {
            list.add(edge);
        }
        Collections.sort(list, new DependencyReportToEdgeComparator());
        ExecutionStatistics.get().end(NAME);
        return list;
    }
}
