package airhacks.ebank.HEX_core.domain;

import java.math.BigDecimal;

import airhacks.ebank.HEX_adapter.in.rest.transaction.TransactionCarrierDto;
import airhacks.ebank.HEX_adapter.in.rest.transaction.TransactionInMapper;

/// Closed set of transaction kinds. [TransactionInMapper#from(TransactionCarrierDto)] is the single
/// place where the wire format becomes a domain transaction; sealing forces
/// the processor to handle every kind.
public sealed interface Transaction permits Transaction.Debit, Transaction.Deposit{
    BigDecimal amount();
    record Debit(BigDecimal amount) implements Transaction {}
    record Deposit(BigDecimal amount) implements Transaction {}

}