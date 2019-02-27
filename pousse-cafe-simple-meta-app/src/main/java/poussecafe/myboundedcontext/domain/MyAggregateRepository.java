package poussecafe.myboundedcontext.domain;

import poussecafe.domain.Repository;

/*
 * The Repository is responsible for providing a shallow (i.e. with no data nor key defined) instance of Aggregate.
 * It also interacts with configured data access which hides the used storage technology.
 */
public class MyAggregateRepository extends Repository<MyAggregate, MyAggregateKey, MyAggregate.Attributes> {

}
