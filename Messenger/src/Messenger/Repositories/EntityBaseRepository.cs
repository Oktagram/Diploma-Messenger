using System.Collections.Generic;
using System.Linq;
using Messenger.Contexts;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using System.Linq.Expressions;
using System;

namespace Messenger.Repositories
{
    public class EntityBaseRepository<T> : IEntityBaseRepository<T>
        where T : class, new()
    {
        private MessengerContext _context;

        public EntityBaseRepository(MessengerContext context)
        {
            _context = context;
        }

        public void Add(T item)
        {
            EntityEntry dbEntityEntry = _context.Entry<T>(item);
            _context.Set<T>().Add(item);
            _context.SaveChanges();
        }

        public void Commit()
        {
            _context.SaveChanges();
        }

        public virtual int Count()
        {
            return _context.Set<T>().Count();
        }

        public T Find(int id)
        {
            return _context.Set<T>().Find(id);
        }

        public virtual IEnumerable<T> FindBy(Expression<Func<T, bool>> predicate)
        {
            return _context.Set<T>().Where(predicate);
        }

        public T GetSingle(Expression<Func<T, bool>> predicate)
        {
            return _context.Set<T>().FirstOrDefault(predicate);
        }

        public T GetSingle(Expression<Func<T, bool>> predicate, params Expression<Func<T, object>>[] includeProperties)
        {
            IQueryable<T> query = _context.Set<T>();
            foreach (var includeProperty in includeProperties)
            {
                query = query.Include(includeProperty);
            }

            return query.Where(predicate).FirstOrDefault();
        }

        public IEnumerable<T> GetAll()
        {
            return _context.Set<T>().AsEnumerable();
        }

        public virtual IEnumerable<T> AllIncluding(params Expression<Func<T, object>>[] includeProperties)
        {
            IQueryable<T> query = _context.Set<T>();
            foreach (var includeProperty in includeProperties)
            {
                query = query.Include(includeProperty);
            }
            return query.AsEnumerable();
        }

        public void Remove(int id)
        {
            EntityEntry dbEntityEntry = _context.Entry(Find(id));
            dbEntityEntry.State = EntityState.Deleted;
            _context.SaveChanges();
        }
    }
}
