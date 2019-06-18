using System.Collections;
using UnityEngine;
using Dreamteam.Bomberman.Players;
using Dreamteam.Bomberman.Weapons;
using Dreamteam.Bomberman.PathFinder;
using System.Collections.Generic;


namespace Dreamteam.Bomberman.Enemies
{
    #region "Direction Block"
    public static enum FaceDirection
    {
        Left = -1,
        Right = 1,
    }
    public enum SideDirection
    {
        Down = -1,
        Up = 1
    }
    #endregion


    [RequireComponent(typeof(Animator))]
    public class BasicEnemy : BaseObject, ISetDamage
    {
        [Tooltip("Здоровье")]
        [SerializeField] protected int health;
        [Tooltip("Скорость передвижения")]
        [SerializeField] protected float speed = 2.0f;
        [Tooltip("Насколько далеко видно игрока")]
        [SerializeField] protected float playerView;
        [Tooltip("Расстояние для атаки")]
        [SerializeField] protected float attackDis;
        [Tooltip("Погрешность довода до точки пути")]
        public float dist = 0.1f;

        [SerializeField] protected GameObject _markPath;
        protected List<Vector2Int> _pointPath = new List<Vector2Int>();
        protected List<Transform> _listPointsTransforms = new List<Transform>();

        protected bool isAlive = true;
        protected bool isMoving = true;
        protected bool isAttack = false;

        protected Animator animator;

        protected PathFinderController pathfinder;

        protected Transform _view;
        protected Transform _targetPlayer;
        [SerializedField]
        protected Transform _target;

        protected Vector2 _directionMove = Vector2.zero;
        protected FaceDirection faceDirection;
        protected SideDirection sideDirection;

        
        protected virtual void Attack() { }
        protected virtual void Dead() => this.isAlive = false;
        protected virtual void Move() {
            Vector2 dir;
            if (!isAttack)
            {
                dir = new Vector2(_directionMove.x * speed, _directionMove.y * speed);
            }
            else
            {
                dir = Vector2.zero;
            }
            //TODO: Надо подумать над конечным вариантом движения
            transform.position += new Vector3(dir.x, dir.y, 0);
            if (Vector2.Distance(transform.position, _target.position) < dist) NextStep();


        }
        protected virtual void SetDamage(int damage) {
            health -= damage;
            if (health <= 0) Dead();
        }


        protected virtual void Awake()=>_view = transform.Find("view");
        protected override void Start()
        {
            base.Start();

            anim = GetComponent<Animator>();

            if (attackDis > playerView) attackDis = playerView;

            _pathFinderController = Main.Instance.PathFinderController;
        }

        protected virtual void OnUpdate() {
            Move();
            if (isAttack) Attack();
        }
        protected virtual void OnFixedUpdate() { }

        protected virtual void NewPath() {
            if (isMoving && !isAttack)
            {
                var freecells = pathfinder.GetFreeCells(transform.position);
                _pointPath = pathfinder.Search(transform.position, (pathfinder.MaxLeft(freecells).x==transform.position.x)?
                                                                    pathfinder.MaxLeft(freecells): pathfinder.MaxRight(freecells));
            }
            else {
                _pointPath = pathfinder.Search(transform.position, _targetPlayer.position);
            }
            NextStep();
        }
        protected virtual void NextStep() {
            if (_pointsPath.Count > 0)
            {
                isMoving = true;
                for (var i = 0; i < _listPointsTransforms.Count; i++)
                {
                    var temp = _listPointsTransforms[i];
                    _listPointsTransforms[i] = null;
                    Destroy(temp.gameObject);
                }
                _listPointsTransforms.Clear();
                if (_pointTarget)
                    Destroy(_pointTarget.gameObject);

                var point = (Vector3)_pointsPath[_pointsPath.Count - 1];
                point += _pathFinderController.GridCellSize / 2;

                for (var i = 0; i < _pointsPath.Count; i++)
                {
                    var temp = (Vector3)_pointsPath[i];
                    temp += _pathFinderController.GridCellSize / 2;
                    var mark = Instantiate(_markPath, temp, Quaternion.identity);
                    _listPointsTransforms.Add(mark.transform);
                    marks.Add(mark);
                }
                _target = _listPointsTransforms[0];
            }
            else
            {
                isMoving = false;
                NewPath();
            }
        }

        protected virtual void UpdatePathMarks() { }

        protected virtual void FindPlayer() { }


        #region "Change Direction Methods"
        /// <summary>
        /// Разворот персонажа по вертикали
        /// </summary>
        private void FlipDirection()
        {
            faceDirection = (FaceDirection)((int)faceDirection * -1f);

            Vector3 newScale = _view.localScale;
            newScale.x *= -1f;
            _view.localScale = newScale;
        }
        /// <summary>
        /// Смена вида по горизонтали
        /// </summary>
        private void FlipSide()
        {
            sideDirection = (SideDirection)((int)sideDirection * -1f);
            //Смена лица/спины после добавления анимации
        }
        #endregion

        /// <summary>
        /// При столкновении с другими противниками, смена целей
        /// </summary>
        protected virtual void OnCollisionEnter2D(Collision2D collision)
        {
            if (collision.transform.GetComponent<BaseEnemy>()) _target = null;
            if (collision.transform.GetComponent<BaseAmmunition>())
            {
                _target = null;
                _pathFinderController.MapWithBombs();
            }

        }

        protected virtual void SetAnimatorParam<T>(string ParamName, T val) {
            //TODO: Дописать запуск анимаций через аниматор
        }
    }

}
