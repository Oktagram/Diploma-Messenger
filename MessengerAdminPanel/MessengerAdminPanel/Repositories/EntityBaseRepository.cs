using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Linq.Expressions;

namespace MessengerAdminPanel.Repositories
{
	public class EntityBaseRepository<T> : IEntityBaseRepository<T> where T : class, new()
	{
		protected readonly MessengerContext _context;

		public EntityBaseRepository(MessengerContext context)
		{
			_context = context;
		}

		public void Add(T item)
		{
			_context.Refresh();
			_context.Set<T>().Add(item);
		}
		
		public virtual int Count()
		{
			_context.Refresh();
			return _context.Set<T>().Count();
		}

		public T Find(int id)
		{
			_context.Refresh();
			return _context.Set<T>().Find(id);
		}

		public virtual IEnumerable<T> FindBy(Expression<Func<T, bool>> predicate)
		{
			_context.Refresh();
			return _context.Set<T>().Where(predicate);
		}

		public T GetSingle(Expression<Func<T, bool>> predicate)
		{
			_context.Refresh();
			return _context.Set<T>().FirstOrDefault(predicate);
		}

		public T GetSingle(Expression<Func<T, bool>> predicate, params Expression<Func<T, object>>[] includeProperties)
		{
			_context.Refresh();

			IQueryable<T> query = _context.Set<T>();

			foreach (var includeProperty in includeProperties)
			{
				query = query.Include(includeProperty);
			}

			return query.Where(predicate).FirstOrDefault();
		}

		public IEnumerable<T> GetAll()
		{
			_context.Refresh();
			return _context.Set<T>().AsEnumerable();
		}

		public virtual IEnumerable<T> AllIncluding(params Expression<Func<T, object>>[] includeProperties)
		{
			_context.Refresh();

			IQueryable<T> query = _context.Set<T>();

			foreach (var includeProperty in includeProperties)
			{
				query = query.Include(includeProperty);
			}

			return query.AsEnumerable();
		}

		public void Remove(int id)
		{
			_context.Refresh();

			var dbEntityEntry = _context.Entry(Find(id));

			dbEntityEntry.State = EntityState.Deleted;
		}
	}
}