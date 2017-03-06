using System;
using System.Collections.Generic;
using System.Linq.Expressions;

namespace MessengerAdminPanel
{
	public interface IEntityBaseRepository<T> where T : class, new()
	{
		void Add(T item);
		IEnumerable<T> GetAll();
		IEnumerable<T> AllIncluding(params Expression<Func<T, object>>[] includeProperties);
		T GetSingle(Expression<Func<T, bool>> predicate, params Expression<Func<T, object>>[] includeProperties);
		T GetSingle(Expression<Func<T, bool>> predicate);
		T Find(int id);
		IEnumerable<T> FindBy(Expression<Func<T, bool>> predicate);
		void Remove(int id);
		void Commit();
		int Count();
	}
}
